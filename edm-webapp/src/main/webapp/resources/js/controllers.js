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
			$location.path("/node/").search("path", $scope.librairies[0].name);
		}
	});
}

function NodeTreeviewController($scope, $http, $location, $routeParams, Node) {

	$scope.addNode = function(node, parentNode) {
		console.debug("Append child : " + node.id);

		var appendNode = $scope.kendoTreeview.append({
			text : node.name
		}, parentNode);

		appendNode.data('node-children-are-loaded', false);	// did I already load my children ?
		appendNode.data('nodedata', node); 					// store the DTO
		$(appendNode[0]).attr('data-nodeid', node.id); 		// explicitly id showing

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

			var nodeId = node.data('nodedata').id;

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
	
	$scope.getDocumentNodeFilePath = function(node) {
		var kendoAppendNode = $scope.treeview.find('[data-nodeid="' + node.id + '"]');
		return "/" + $scope.getNodePath(kendoAppendNode) + "." + node.fileExtension;
	}

	$scope.onNodeSelect = function(e) {
		var node = $(e.node);

		var nodeId = node.data('nodedata').id;
		console.debug("Selecting: " + nodeId);

		$scope.$apply(function() {
			$scope.currentNode = node.data('nodedata');
		});

		$scope.loadNodeChildrenAndExpand(node);

		var nodePath = $scope.getNodePath(node);
		console.debug("path = " + nodePath);

		$location.search('path', nodePath);
	};

	// loop to load all children nodes
	// [int] 				i		 the current node index
	// [int] 				max		 the max count
	// [kendoui tree node]	parent	 the parent node
	$scope.recursiveNodeLoader = function(currentIndex, max, parent) {

		if (currentIndex == max) { // break
			// select the last node
			$scope.kendoTreeview.select(parent);
			return;
		}

		var currentNodePath = $scope.nodePathArray.slice(0, currentIndex + 1).join('/');

		Node.get({
			nodepath : currentNodePath
		}, function(response) {

			if (response.id === null) { // break
				$scope.kendoTreeview.select(parent);
				return;
			}

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

	$scope.onDragStart = function(e) {
		console.log("Started dragging " + this.text(e.sourceNode));
	}

	$scope.onDrop = function(e) {
		console.log("Dropped " + this.text(e.sourceNode) + " (" + (e.valid ? "valid" : "invalid") + ")");
	}

	$scope.onDragEnd = function(e) {
		console.log("Finished dragging " + this.text(e.sourceNode));
	}

	// main

	$scope.$on('$viewContentLoaded', function() {
		
		$scope.treeview = $("#edm-treeview");
		$scope.treeview .kendoTreeView({
			loadOnDemand : false,
			select : $scope.onNodeSelect,
			dragAndDrop : false,
			/* drag & drop events */
			dragstart : $scope.onDragStart,
			drop : $scope.onDrop,
			dragend : $scope.onDragEnd
		});

		$scope.kendoTreeview = $scope.treeview.data("kendoTreeView");

		$scope.nodePathArray = $routeParams.path.split('/');
		console.debug($scope.nodePathArray);
		$scope.recursiveNodeLoader(0, $scope.nodePathArray.length, null);
	});

}
