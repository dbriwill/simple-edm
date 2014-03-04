angular.module('edmApp', ['ngRoute', 'nodeService', 'libraryService', 'directoryService', 'documentService', 'blueimp.fileupload', 'notificationService']).
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
        	when('/document/search', // /document/search?q=bazinga
        			{
						templateUrl:'resources/views/document-search-result.html',
						controller:DocumentSearchController
        			}
        	).
        	when('/node/', //  /node/?path=Document/Directory
        			{
        				templateUrl:'resources/views/node-treeview.html',
        				controller:NodeTreeviewController,
        				reloadOnSearch: false
        			}
        	).
            otherwise(
            		{
            			redirectTo:'/library/list'
            		}
            );
}]);
