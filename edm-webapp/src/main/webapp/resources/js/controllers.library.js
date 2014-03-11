function LibraryListController($scope, $location, Library) {
	$scope.librairies = Library.query(function(response) {
		// auto focus on main library if only one is available
		if ($scope.librairies.length == 1) {
			$location.path("/node/").search("path", $scope.librairies[0].name);
		}
	});
}
