angular.module('edmApp', ['ngRoute', 'nodeService', 'messageService', 'libraryService', 'directoryService', 'documentService', 'blueimp.fileupload']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
        	when('/library/list',	
        			{
        				templateUrl:'resources/views/library-list.html',   
        				controller:LibraryListController
        			}
        	).
        	when('/document/add', //  /document/add?root=Document/Directory
        			{
        				templateUrl:'resources/views/document-new.html',
        				controller:DocumentNewController
        			}
        	).
        	when('/document/edit', //  /document/edit?path=Document/Directory
        			{
        				templateUrl:'resources/views/document-new.html',
        				controller:DocumentEditController
        			}
        	).
        	when('/node/', //  /node/?path=Document/Directory
        			{
        				templateUrl:'resources/views/node-treeview.html',
        				controller:NodeTreeviewController,
        				reloadOnSearch: false
        			}
        	).
            when('/message/list',
            		{
            			templateUrl:'resources/views/message-list.html',
            			controller:MessageListController
            		}
            ).
            when('/message/new',
            		{
            			templateUrl:'resources/views/message-new.html',
            			controller:MessageNewController
            		}
            ).
            when('/message/:id',
            		{
            			templateUrl:'resources/views/message-detail.html',
            			controller:MessageDetailController
            		}
            ).
            otherwise(
            		{
            			redirectTo:'/library/list'
            		}
            );
}]);
