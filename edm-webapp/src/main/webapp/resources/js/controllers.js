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

		var appendNode = $scope.kendoTreeview.append({
			text : node.name
		}, parentNode);

		appendNode.data('node-children-are-loaded', false);
		appendNode.data('nodeid', node.id);
		$(appendNode[0]).attr('data-nodeid', node.id);
		
		if (node.edmNodeType === 'LIBRARY') {
			appendNode.find('.k-bot').prepend('<span class="k-sprite rootfolder"></span>');
		} else if (node.edmNodeType === 'DIRECTORY') {
			appendNode.find('.k-bot').prepend('<span class="k-sprite folder"></span>');
		} else {
			appendNode.find('.k-bot').prepend('<span class="k-sprite image"></span>');
		}

		return appendNode;
	};

	$scope.loadNodeChildrenAndExpand = function(node) {
		if (node.data('node-children-are-loaded') === false) {
			console.debug('loading children...');

			var nodeId = node.data('nodeid');

			$http.get('/node/childs/' + nodeId).success(function(data, status, headers, config) {
				
				for (var i = 0; i < data.length; i++) {
					$scope.addNode(data[i], node);
				}

			}).error(function(data, status, headers, config) {
				console.error('Failed to retrieve child of node ' + nodeId);
				// TODO : feedback for user
			});

			node.data('node-children-are-loaded', true);
		}
	};

	$scope.getNodePath = function(node) {
        var kitems = $(node).add($(node).parentsUntil('.k-treeview', '.k-item'));

        var texts = $.map(kitems, function(kitem) {
            return $(kitem).find('>div span.k-in').text();
        });
		
		return texts.join("/");
	};
	
	$scope.onNodeSelect = function(e) {
		var node = $(e.node);

		var nodeId = node.data('nodeid');
		console.debug("Selecting: " + nodeId);
		
		$scope.loadNodeChildrenAndExpand(node);

		var nodePath = $scope.getNodePath(node);
		console.debug("/node/" + nodePath);
		$location.path("/node/" + nodePath);
	};

	// loop to load all children nodes
	// [int] 				i		 the current node index
	// [int] 				max		 the max count
	// [kendoui tree node]	parent	 the parent node
	$scope.recursiveNodeLoader = function(currentIndex, max, parent) {
		if (currentIndex == max) {
			// select the last node
			$scope.kendoTreeview.select(parent);
			return;
		}

		var currentNodePath = $scope.nodePathArray.slice(0, currentIndex + 1).join('/');

		Node.get({
			nodepath : currentNodePath
		}, function(response) {
		
			// node is loaded ? Just wanna know if data-nodeid already exists...
			var kendoAppendNode = $scope.treeview.find('[data-nodeid="' + response.id + '"]');
			
			if (kendoAppendNode.length === 0) {
				kendoAppendNode = $scope.addNode(response, parent);
			}
			
			// always show children of the last selection
			$scope.loadNodeChildrenAndExpand(kendoAppendNode);

			// pass to the next children
			$scope.recursiveNodeLoader(currentIndex + 1, max, kendoAppendNode);
		});
	};

	// main

	$scope.$on('$viewContentLoaded', function() {
		$("#treeview").kendoTreeView({
			loadOnDemand : false,
			select : $scope.onNodeSelect
		});

		$scope.treeview = $("#treeview");
		$scope.kendoTreeview = $scope.treeview.data("kendoTreeView");

		$scope.nodePathArray = $routeParams.path.split('/');
		console.debug($scope.nodePathArray);
		$scope.recursiveNodeLoader(0, $scope.nodePathArray.length, null);
	});

}
