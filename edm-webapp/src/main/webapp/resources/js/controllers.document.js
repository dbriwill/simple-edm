function DocumentNewController($scope, $window, $location, $routeParams, Document) {
	
	$scope.parentNode = $routeParams.root;
	
	$scope.back = function() {
		$window.history.back();
	}
	
//	$scope.submit = function() {
//		Document.save($scope.message, function(message) {
//			$location.path('/');
//		});
//	};
}
