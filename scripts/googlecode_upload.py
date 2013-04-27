#!/usr/bin/env python
#
# Copyright 2006, 2007 Google Inc. All Rights Reserved.
# Author: danderson@google.com (David Anderson)
#
# Script for uploading files to a Google Code project.
#
# This is intended to be both a useful script for people who want to
# streamline project uploads and a reference implementation for
# uploading files to Google Code projects.
#
# To upload a file to Google Code, you need to provide a path to the
# file on your local machine, a small summary of what the file is, a
# project name, and a valid account that is a member or owner of that
# project.  You can optionally provide a list of labels that apply to
# the file.  The file will be uploaded under the same name that it has
# in your local filesystem (that is, the "basename" or last path
# component).  Run the script with '--help' to get the exact syntax
# and available options.
#
# Note that the upload script requests that you enter your
# googlecode.com password.  This is NOT your Gmail account password!
# This is the password you use on googlecode.com for committing to
# Subversion and uploading files.  You can find your password by going
# to http://code.google.com/hosting/settings when logged in with your
# Gmail account. If you have already committed to your project's
# Subversion repository, the script will automatically retrieve your
# credentials from there (unless disabled, see the output of '--help'
# for details).
#
# If you are looking at this script as a reference for implementing
# your own Google Code file uploader, then you should take a look at
# the upload() function, which is the meat of the uploader.  You
# basically need to build a multipart/form-data POST request with the
# right fields and send it to https://PROJECT.googlecode.com/files .
# Authenticate the request using HTTP Basic authentication, as is
# shown below.
#
# Licensed under the terms of the Apache Software License 2.0:
#  http://www.apache.org/licenses/LICENSE-2.0
#
# Questions, comments, feature requests and patches are most welcome.
# Please direct all of these to the Google Code users group:
#  http://groups.google.com/group/google-code-hosting

"""Google Code file uploader script.
"""

__author__ = 'danderson@google.com (David Anderson)'

import httplib
import os
import os.path
import optparse
import getpass
import base64
import sys
import urllib2
import urllib
import socket

#-------------------------------------------------------------------------------

class ProxyHTTPConnection(httplib.HTTPConnection):
    _ports = {'http' : 80, 'https' : 443}

    def request(self, method, url, body=None, headers={}):
        #request is called before connect, so can interpret url and get
        #real host/port to be used to make CONNECT request to proxy
        proto, rest = urllib.splittype(url)
        if proto is None:
            raise ValueError, "unknown URL type: %s" % url
        #get host
        host, rest = urllib.splithost(rest)
        #try to get port
        host, port = urllib.splitport(host)
        #if port is not defined try to get from proto
        if port is None:
            try:
                port = self._ports[proto]
            except KeyError:
                raise ValueError, "unknown protocol for: %s" % url
        self._real_host = host
        self._real_port = port
        httplib.HTTPConnection.request(self, method, url, body, headers)

    def connect(self):
        httplib.HTTPConnection.connect(self)
        #send proxy CONNECT request
        self.send("CONNECT %s:%d HTTP/1.0\r\n\r\n" % (self._real_host, self._real_port))
        #expect a HTTP/1.0 200 Connection established
        response = self.response_class(self.sock, strict=self.strict, method=self._method)
        (version, code, message) = response._read_status()
        #probably here we can handle auth requests...
        if code != 200:
            #proxy returned and error, abort connection, and raise exception
            self.close()
            raise socket.error, "Proxy connection failed: %d %s" % (code, message.strip())
        #eat up header block from proxy....
        while True:
            #should not use directly fp probablu
            line = response.fp.readline()
            if line == '\r\n': break

#-------------------------------------------------------------------------------

class ProxyHTTPSConnection(ProxyHTTPConnection):
    default_port = 443

    def __init__(self, host, port = None, key_file = None, cert_file = None, strict = None):
        ProxyHTTPConnection.__init__(self, host, port)
        self.key_file = key_file
        self.cert_file = cert_file

    def connect(self):
        ProxyHTTPConnection.connect(self)
        #make the sock ssl-aware
        ssl = socket.ssl(self.sock, self.key_file, self.cert_file)
        self.sock = httplib.FakeSocket(self.sock, ssl)

#-------------------------------------------------------------------------------

class ConnectHTTPHandler(urllib2.HTTPHandler):

    def do_open(self, http_class, req):
        return urllib2.HTTPHandler.do_open(self, ProxyHTTPConnection, req)

#-------------------------------------------------------------------------------

class ConnectHTTPSHandler(urllib2.HTTPSHandler):

    def do_open(self, http_class, req):
        return urllib2.HTTPSHandler.do_open(self, ProxyHTTPSConnection, req)

#-------------------------------------------------------------------------------


def upload(file, project_name, user_name, password, summary, labels=None):
  """Upload a file to a Google Code project's file server.

  Args:
    file: The local path to the file.
    project_name: The name of your project on Google Code.
    user_name: Your Google account name.
    password: The googlecode.com password for your account.
              Note that this is NOT your global Google Account password!
    summary: A small description for the file.
    labels: an optional list of label strings with which to tag the file.

  Returns: a tuple:
    http_status: 201 if the upload succeeded, something else if an
                 error occured.
    http_reason: The human-readable string associated with http_status
    file_url: If the upload succeeded, the URL of the file on Google
              Code, None otherwise.
  """
  # The login is the user part of user@gmail.com. If the login provided
  # is in the full user@domain form, strip it down.
  if user_name.endswith('@gmail.com'):
    user_name = user_name[:user_name.index('@gmail.com')]

  form_fields = [('summary', summary)]
  if labels is not None:
    form_fields.extend([('label', l.strip()) for l in labels])

  content_type, body = encode_upload_request(form_fields, file)

  upload_host = '%s.googlecode.com' % project_name
  upload_uri = '/files'
  auth_token = base64.b64encode('%s:%s'% (user_name, password))
  headers = {
    'Authorization': 'Basic %s' % auth_token,
    'User-Agent': 'Googlecode.com uploader v0.9.4',
    'Content-Type': content_type,
    }

  target_url = 'https://' + upload_host + upload_uri
  
  if os.environ.get('HTTP_PROXY') != None :
    opener = urllib2.build_opener(ConnectHTTPSHandler, ConnectHTTPSHandler)
    urllib2.install_opener(opener)

  req = urllib2.Request(target_url, body, headers)
  
  if os.environ.get('HTTP_PROXY') != None :
    req.set_proxy(os.environ.get('HTTP_PROXY')[7:], 'http')
	
  server_response_code      = 201
  serveur_response_label    = ''
  serveur_response_location = None
  try:
    response = urllib2.urlopen(req)
    serveur_response_location = response.info().getheader('Location')
  except urllib2.HTTPError, e:
    server_response_code = e
	
  return server_response_code, serveur_response_label, serveur_response_location


def encode_upload_request(fields, file_path):
  """Encode the given fields and file into a multipart form body.

  fields is a sequence of (name, value) pairs. file is the path of
  the file to upload. The file will be uploaded to Google Code with
  the same file name.

  Returns: (content_type, body) ready for httplib.HTTP instance
  """
  BOUNDARY = '----------Googlecode_boundary_reindeer_flotilla'
  CRLF = '\r\n'

  body = []

  # Add the metadata about the upload first
  for key, value in fields:
    body.extend(
      ['--' + BOUNDARY,
       'Content-Disposition: form-data; name="%s"' % key,
       '',
       value,
       ])

  # Now add the file itself
  file_name = os.path.basename(file_path)
  f = open(file_path, 'rb')
  file_content = f.read()
  f.close()

  body.extend(
    ['--' + BOUNDARY,
     'Content-Disposition: form-data; name="filename"; filename="%s"'
     % file_name,
     # The upload server determines the mime-type, no need to set it.
     'Content-Type: application/octet-stream',
     '',
     file_content,
     ])

  # Finalize the form body
  body.extend(['--' + BOUNDARY + '--', ''])

  return 'multipart/form-data; boundary=%s' % BOUNDARY, CRLF.join(body)


def upload_find_auth(file_path, project_name, summary, labels=None,
                     user_name=None, password=None, tries=3):
  """Find credentials and upload a file to a Google Code project's file server.

  file_path, project_name, summary, and labels are passed as-is to upload.

  Args:
    file_path: The local path to the file.
    project_name: The name of your project on Google Code.
    summary: A small description for the file.
    labels: an optional list of label strings with which to tag the file.
    config_dir: Path to Subversion configuration directory, 'none', or None.
    user_name: Your Google account name.
    tries: How many attempts to make.
  """
  if user_name is None or password is None:
    from netrc import netrc
    authenticators = netrc().authenticators("code.google.com")
    if authenticators:
      if user_name is None:
        user_name = authenticators[0]
      if password is None:
        password = authenticators[2]

  while tries > 0:
    if user_name is None:
      # Read username if not specified or loaded from svn config, or on
      # subsequent tries.
      sys.stdout.write('Please enter your googlecode.com username: ')
      sys.stdout.flush()
      user_name = sys.stdin.readline().rstrip()
    if password is None:
      # Read password if not loaded from svn config, or on subsequent tries.
      print 'Please enter your googlecode.com password.'
      print '** Note that this is NOT your Gmail account password! **'
      print 'It is the password you use to access Subversion repositories,'
      print 'and can be found here: http://code.google.com/hosting/settings'
      password = getpass.getpass()

    status, reason, url = upload(file_path, project_name, user_name, password,
                                 summary, labels)
    # Returns 403 Forbidden instead of 401 Unauthorized for bad
    # credentials as of 2007-07-17.
    if status in [httplib.FORBIDDEN, httplib.UNAUTHORIZED]:
      # Rest for another try.
      user_name = password = None
      tries = tries - 1
    else:
      # We're done.
      break

  return status, reason, url


def main():
  parser = optparse.OptionParser(usage='googlecode-upload.py -s SUMMARY '
                                 '-p PROJECT [options] FILE')
  parser.add_option('-s', '--summary', dest='summary',
                    help='Short description of the file')
  parser.add_option('-p', '--project', dest='project',
                    help='Google Code project name')
  parser.add_option('-u', '--user', dest='user',
                    help='Your Google Code username')
  parser.add_option('-w', '--password', dest='password',
                    help='Your Google Code password')
  parser.add_option('-l', '--labels', dest='labels',
                    help='An optional list of comma-separated labels to attach '
                    'to the file')

  options, args = parser.parse_args()

  if not options.summary:
    parser.error('File summary is missing.')
  elif not options.project:
    parser.error('Project name is missing.')
  elif len(args) < 1:
    parser.error('File to upload not provided.')
  elif len(args) > 1:
    parser.error('Only one file may be specified.')

  file_path = args[0]

  if options.labels:
    labels = options.labels.split(',')
  else:
    labels = None

  status, reason, url = upload_find_auth(file_path, options.project,
                                         options.summary, labels,
                                         options.user, options.password)
  if url:
    print 'The file was uploaded successfully.'
    print 'URL: %s' % url
    return 0
  else:
    print 'An error occurred. Your file was not uploaded.'
    print 'Google Code upload server said: %s (%s)' % (reason, status)
    return 1


if __name__ == '__main__':
  sys.exit(main())
  


  
