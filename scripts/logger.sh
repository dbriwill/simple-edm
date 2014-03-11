#!/bin/sh

# @params neutral message
show_neutral_message(){
	echo -e "\033[0;36;40m$@\033[0m"
}
# @params information message
show_information_message(){
	echo -e "\033[7;32;40m$@\033[0m"
}
# @params error message
show_error_message(){
	echo -e "\033[7;31;47m$@\033[0m"
}

