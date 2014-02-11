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

function NodeTreeviewController($scope, $http, $location, $routeParams, Node) {
	
	$scope.addNode = function(node, parentNode) {
		console.debug("Append child : " + node.id);

		var appendNode = $scope.treeview.append({
			text : node.name
		}, parentNode);

		appendNode.data('node-children-are-loaded', false);
		appendNode.data('nodeid', node.id);

		if (node.edmNodeType === 'LIBRARY') {
			appendNode.find('.k-bot').prepend(
					'<span class="k-sprite rootfolder"></span>');
		} else if (node.edmNodeType === 'DIRECTORY') {
			appendNode.find('.k-bot').prepend(
					'<span class="k-sprite folder"></span>');
		} else {
			appendNode.find('.k-bot').prepend(
					'<span class="k-sprite image"></span>');
		}
		
		return appendNode;
	};

	$scope.loadNodeChildrenAndExpand = function(node) {
		if (node.data('node-children-are-loaded') === false) {
			console.debug('loading children...');

			var nodeId = node.data('nodeid');

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
	};

	$scope.onNodeSelect = function(e) {
		var node = $(e.node);

		var nodeId = node.data('nodeid');
		console.debug("Selecting: " + nodeId);

		$scope.loadNodeChildrenAndExpand(node);

		//$location.path($location.path() + "/" + scope.rootNode.name);
	};

	
	// loop to load all childs nodes
	// i [int] is the current node index, max [int] the max count, parent the parent node
	$scope.recursiveNodeLoader = function(i, max, parent) {
		if (i == max) {
			return;
		}
		
		var currentNodePath = $scope.nodePathArray.slice(0, i+1).join('/');
		
        Node.get({
			nodepath : currentNodePath
		}, function(response) {
			var appendNode = $scope.addNode(response, parent);
			$scope.recursiveNodeLoader(i+1, max, appendNode);
			$scope.treeview.select(appendNode);
		});
	};
	
	
	// main
	
	$scope.$on('$viewContentLoaded', function() {
		$("#treeview").kendoTreeView({
			loadOnDemand : false,
			select : $scope.onNodeSelect
		});
		$scope.treeview = $("#treeview").data("kendoTreeView");
		
		$scope.nodePathArray = $routeParams.path.split('/');
		console.debug($scope.nodePathArray);
		$scope.recursiveNodeLoader(0, $scope.nodePathArray.length, null);
	});
	
}
