
function MessageListController($scope, $location, Message) {
    $scope.messages = Message.query();
 
    $scope.gotoMessageNewPage = function () {
        $location.path("/message/new")
    };
    
    $scope.deleteMessage = function (message) {
        message.$delete({'id':message.id}, function () {
            $location.path('/');
        });
    };
}


function MessageDetailController($scope, $routeParams, Message) {
    $scope.message = Message.get({id:$routeParams.id});
}


function MessageNewController($scope, $location, Message) {
    $scope.submit = function () {
    	Message.save($scope.message, function (message) {
            $location.path('/');
        });
    };
    $scope.gotoMessageListPage = function () {
        $location.path("/")
    };
}

function LibraryListController($scope, $location, Library) {
    $scope.librairies = Library.query(function(response) {
    	// auto focus on main library if only one is avaliable
    	if ($scope.librairies.length == 1) {
    		$location.path("/node/" + $scope.librairies[0].id);
    	}
    });
}

function LibraryTreeviewController($scope, $routeParams, Library) {
    $scope.library = Library.get({id:$routeParams.id});
}
