angular.module('edmApp', ['ngRoute', 'nodeService', 'messageService', 'libraryService']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
        	when('/library/list',	
        			{
        				templateUrl:'resources/views/library-list.html',   
        				controller:LibraryListController
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
