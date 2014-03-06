function NodeTreeviewController($scope, $http, $location, $routeParams, Node, Library, Directory, Document, notification) {

	$scope.newDirectory = {};
	
	// Map<String, DTO> : nodeId, nodeDto
	$scope.nodeMap = {};

	
	$scope.getServiceForNode = function(node) {
		if (node.edmNodeType === 'LIBRARY') {
			return Library;
		} else if (node.edmNodeType === 'DIRECTORY') {
			return Directory;
		}
		return Document;
	}

	// get the UI representation of some node
	$scope.getUINodeFromNode = function(node) {
		if (node === null) {
			return null;
		}
		return $scope.treeview.find('[data-nodeid="' + node.id + '"]');
	}
	
	$scope.addNode = function(node, parentNode) {
		console.debug("Append child : " + node.id);

		var appendNode = $scope.kendoTreeview.append({
			text : node.name
		}, $scope.getUINodeFromNode(parentNode));

		$(appendNode[0]).attr('data-children-loaded', false);	// did I already load my children ?
		$(appendNode[0]).attr('data-nodeid', node.id); 			// explicitly id showing
		$scope.nodeMap[node.id] = node;							// store DTO value
		
		if (node.edmNodeType === 'LIBRARY') {
			$scope.currentLibrary = node;
			appendNode.find('.k-bot').prepend('<span class="k-sprite rootfolder"></span>');
		} else if (node.edmNodeType === 'DIRECTORY') {
			appendNode.find('.k-bot').prepend('<span class="k-sprite folder"></span>');
		} else {
			appendNode.find('.k-bot').prepend('<span class="k-sprite image"></span>');
		}
	};

	$scope.loadNodeChildrenAndExpand = function(node) {
		if ($scope.getUINodeFromNode(node).attr('data-children-loaded') == 'false') {
			console.debug('loading children...');

			$http.get('/node/childs/' + node.id).success(function(data, status, headers, config) {

				for (var i = 0; i < data.length; i++) {
					$scope.addNode(data[i], node);
				}

			}).error(function(data, status, headers, config) {
				console.error('Failed to retrieve child of node ' + node.id);
				notification.add('ERROR', "La récupération des sous-documents a échouée... Réessayez plus tard !");
			});

			$scope.getUINodeFromNode(node).attr('data-children-loaded', true);
		}
	};
	
	$scope.applyChanges = function() {
		$scope.$apply();
	}
	
	$scope.selectNode = function(node) {
		// reset selection
		$scope.newDirectory = {};
		
		console.debug("Selecting: " + node.id);
		$scope.loadNodeChildrenAndExpand(node);

		var nodePath = node.nodePath;
		console.debug("path = " + nodePath);
		$location.search('path', nodePath);

		$scope.currentNode = node;
	}
	
	$scope.onNodeSelect = function(e) { // e is a kendoNode
		var node = $scope.nodeMap[$(e.node).attr('data-nodeid')];
		$scope.selectNode(node);
		$scope.applyChanges();
	};

	// loop to load all children nodes
	// [int] 				i		 the current node index
	// [int] 				max		 the max count
	// [kendoui tree node]	parent	 the parent node
	$scope.recursiveNodeLoader = function(currentIndex, max, parentNode) {

		if (currentIndex === max) { // break
			// select the last node
			$scope.kendoTreeview.select($scope.getUINodeFromNode(parentNode));
			$scope.selectNode(parentNode);
			return;
		}

		var currentNodePath = $scope.nodePathArray.slice(0, currentIndex + 1).join('/');

		$http.get('/node/path/' + currentNodePath).success(function(response, status, headers, config) {

			if (response.id === null) { // break
				$scope.kendoTreeview.select($scope.getUINodeFromNode(parentNode));
				$scope.selectNode(parentNode);
				return;
			}

			// node is already loaded ?
			if ($scope.nodeMap[response.id] === undefined) {
				$scope.addNode(response, parentNode);
			}

			// always show children of the last selection
			$scope.loadNodeChildrenAndExpand(response);

			// pass to the next children
			$scope.recursiveNodeLoader(currentIndex + 1, max, response);
		});
	};

	$scope.onDragStart = function(e) { // e is a kendoNode
		console.log("Started dragging " + this.text(e.sourceNode));
	}

	$scope.onDrop = function(e) { // e is a kendoNode
		console.log("Dropped " + this.text(e.sourceNode) + " (" + (e.valid ? "valid" : "invalid") + ")");
		
		var sourceNode		= $scope.nodeMap[$(e.sourceNode).attr('data-nodeid')]; 
		var destinationNode = $scope.nodeMap[$(e.destinationNode).attr('data-nodeid')];
		
		if (sourceNode.id === destinationNode.id) {
			console.warn("Same source and target, aborted");
			return;
		}
		
		console.info('Moving node : "' + sourceNode.id + '" to parent "' + destinationNode.id + '"');
		
		sourceNode.parentId = destinationNode.id;
		$scope.getServiceForNode(sourceNode).save(sourceNode, function(node) {
			notification.add('INFO', "Le document a bien été déplacé");
		});
	}

	$scope.onDragEnd = function(e) {
		console.log("Finished dragging " + this.text(e.sourceNode));
	}

	$scope.addNewDirectoryOnCurrentNode = function() {
		console.info("Saving new directory : " + $scope.newDirectory.name);
		$scope.newDirectory.parentId = $scope.currentNode.id;
		Directory.save($scope.newDirectory, function(directory) {
			notification.add('INFO', "Le dossier a bien été ajouté");
			$scope.addNode(directory, $scope.currentNode);
			$scope.selectNode(directory); 
		});
	}
	
	$scope.askForDeleteCurrentNode = function() {
		console.log("wanna delete current node !");
		if ($('#modalPopover').length > 0) { // exists
			$('#modalPopover').modal('show');
		}
		return false;
	}
	
	$scope.deleteCurrentNode = function() { 
		$('#modalPopover').modal('hide');
		var parentId = $scope.currentNode.parentId;
		$scope.getUINodeFromNode($scope.currentNode).remove();
		Node.delete({
			id : $scope.currentNode.id
		}, function(response) {
			notification.add('INFO', "Le document a bien été supprimé");
		});
		$scope.selectNode($scope.nodeMap[parentId]);
	}
	
	$scope.askForRenameCurrentNode = function() {
		console.log("wanna rename current node !");
		if ($('#modalRenamePopover').length > 0) { // exists
			$('#modalRenamePopover').modal();
		}
		return false;
	}
	
	$scope.updateCurrentNode = function() {		
		$('#modalRenamePopover').modal('hide');
		
		$scope.getServiceForNode($scope.currentNode).save($scope.currentNode, function(node) {
			var currentKendoNode = $scope.getUINodeFromNode($scope.currentNode);
			currentKendoNode.find('.k-in').first().text(node.name);
			notification.add('INFO', "Le nom a bien été mis à jour");
			
			// update child nodes
			$.each($scope.getUINodeFromNode($scope.currentNode).find('.k-item'), function(index, value) {
				Node.get({id : $(value).attr('data-nodeid')}, function(response) {
					console.debug("Update child : " + response.id);
					$scope.nodeMap[response.id] = response;	
				});
			});
		});
	}
	
	$scope.searchSubmit = function() {
		console.debug("Search : " + $scope.searchedPattern);
		$location.path('/document/search').search({
			'q' : $scope.searchedPattern
		});
	}
	
	// main

	$scope.$on('$viewContentLoaded', function() {
		
		$scope.treeview = $("#edm-treeview");
		$scope.treeview .kendoTreeView({
			loadOnDemand : false,
			select : $scope.onNodeSelect,
			dragAndDrop : true,
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
