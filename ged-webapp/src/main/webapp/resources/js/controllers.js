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
	
	$scope.rootNode = Node.get({
		nodepath : libraryName
	}, function(response){
		$scope.addNode($scope.rootNode, null);
	});

	$scope.addNode = function(node, parentNode) {
		console.debug("Append child : " + node.id);

		var appendNode = $scope.treeview.append({
			text : node.name
		}, parentNode);

		appendNode.data('node-children-are-loaded', false);
		appendNode.data('nodeid', node.id);

		if (node.gedNodeType === 'LIBRARY' || node.gedNodeType === 'DIRECTORY') {
			appendNode.find('.k-bot').prepend('<span class="k-sprite folder"></span>');
		}
	};

	$scope.onNodeSelect = function(e) {
		var node = $(e.node);

		var nodeId = node.data('nodeid');
		console.debug("Selecting: " + nodeId);

		if (node.data('node-children-are-loaded') === false) {
			console.debug('loading children...');

			$http.get('/node/childs/' + nodeId).success(
					function(data, status, headers, config) {
						var selectedNode = $scope.treeview.select();

						for (var i = 0; i < data.length; i++) {
							$scope.addNode(data[i], selectedNode);
						}

					}).error(function(data, status, headers, config) {
				console.error('Failed to retrieve child of node ' + nodeId);
				// TODO : feedback for user
			});

			node.data('node-children-are-loaded', true);
		}
		
//		$routeParams.path($routeParams.path() + "/" + scope.rootNode.name);
	};

	$scope.$on('$viewContentLoaded', function() {
		$("#treeview").kendoTreeView({
			loadOnDemand : false,
			select : $scope.onNodeSelect
		});
		$scope.treeview = $("#treeview").data("kendoTreeView");
	});
}
