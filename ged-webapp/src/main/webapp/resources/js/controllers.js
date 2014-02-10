function MessageListController($scope, $location, Message) {
	$scope.messages = Message.query();

	$scope.gotoMessageNewPage = function() {
		$location.path("/message/new")
	};

	$scope.deleteMessage = function(message) {
		message.$delete({
			'id' : message.id
		}, function() {
			$location.path('/');
		});
	};
}

function MessageDetailController($scope, $routeParams, Message) {
	$scope.message = Message.get({
		id : $routeParams.id
	});
}

function MessageNewController($scope, $location, Message) {
	$scope.submit = function() {
		Message.save($scope.message, function(message) {
			$location.path('/');
		});
	};
	$scope.gotoMessageListPage = function() {
		$location.path("/")
	};
}

function LibraryListController($scope, $location, Library) {
	$scope.librairies = Library.query(function(response) {
		// auto focus on main library if only one is available
		if ($scope.librairies.length == 1) {
			$location.path("/node/" + $scope.librairies[0].name);
		}
	});
}

function NodeTreeviewController($scope, $http, $routeParams, Node) {
	var nodePath = $routeParams.path.split('/');
	var libraryName = nodePath[0];
	$scope.library = Node.get({
		nodepath : libraryName
	});

	$scope.onNodeSelect = function(e) {
		var node = $(e.node);

		var nodeId = node.data('nodeid');
		console.debug("Selecting: " + nodeId);

		if (node.data('node-children-are-loaded') === false) {
			console.debug('loading children...');

			$http.get('/node/childs/' + nodeId).success(
					function(data, status, headers, config) {

						var treeview = $("#treeview").data("kendoTreeView");
						var selectedNode = treeview.select();

						for (var i = 0; i < data.length; i++) {
							node = data[i];

							console.debug("Append child : " + node.id);
							
							var appendNode = treeview.append({
                                text: node.name
                            }, selectedNode);
							
							appendNode.data('node-children-are-loaded', false);
							appendNode.data('nodeid', node.id);
						}

					}).error(function(data, status, headers, config) {
				console.error('Failed to retrieve child of node ' + nodeId);
				// TODO : feedback for user
			});

			node.data('node-children-are-loaded', true);
		}
	};

	$scope.$on('$viewContentLoaded', function() {
		$("#treeview").kendoTreeView({
			loadOnDemand: false,
			select : $scope.onNodeSelect
		});
	});
}
