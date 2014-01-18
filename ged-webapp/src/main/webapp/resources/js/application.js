angular.module('gedApp', ['messageService']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
        	when('/library/list',	{templateUrl:'resources/views/library-list.html',   controller:LibraryListController}).
            when('/message/list',	{templateUrl:'resources/views/message-list.html',   controller:MessageListController}).
            when('/message/new',	{templateUrl:'resources/views/message-new.html',    controller:MessageNewController}).
            when('/message/:id',	{templateUrl:'resources/views/message-detail.html', controller:MessageDetailController}).
            otherwise({redirectTo:'/library/list'});
}]);