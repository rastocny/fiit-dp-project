package com.mlyncar.dp.transformer.core.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlyncar.dp.analyzer.entity.CombFragment;
import com.mlyncar.dp.analyzer.entity.Message;
import com.mlyncar.dp.analyzer.entity.SeqDiagram;
import com.mlyncar.dp.transformer.core.TransformationEngine;
import com.mlyncar.dp.transformer.entity.Edge;
import com.mlyncar.dp.transformer.entity.EdgeType;
import com.mlyncar.dp.transformer.entity.Graph;
import com.mlyncar.dp.transformer.entity.Node;
import com.mlyncar.dp.transformer.entity.impl.EdgeImpl;
import com.mlyncar.dp.transformer.entity.impl.NodeCombinedFragmentImpl;
import com.mlyncar.dp.transformer.entity.impl.NodeImpl;
import com.mlyncar.dp.transformer.entity.impl.TreeGraph;
import com.mlyncar.dp.transformer.exception.GraphTransformationException;
import com.mlyncar.dp.transformer.exception.MessageTypeException;
import com.mlyncar.dp.transformer.test.TransformationTestHelper;

public class TransformationEngineImpl implements TransformationEngine {

    private final Logger logger = LoggerFactory.getLogger(TransformationEngineImpl.class);

    @Override
    public Graph transformSequenceDiagram(SeqDiagram diagram)
            throws GraphTransformationException {
        Graph graph = initializeGraphStructure(diagram);
        Node previousNode = graph.getRootNode();
        for (Message message : diagram.getMessages()) {
            Node newNode = storeMessageIntoGraph(graph, message, previousNode);
            previousNode = newNode;
        }
        new TransformationTestHelper().printGraph(graph);;
        return graph;
    }

    private Node storeMessageIntoGraph(Graph graph, Message message, Node lastInsertedNode) throws GraphTransformationException {
        try {
            logger.debug("Finding suitable place for node " + message.getName() + " " + message.getTargetLifeline().getName());
            Edge edge = new EdgeImpl(message.getName(), EdgeType.fromCode(message.getType().getCode()));
            if (lastInsertedNode.getParentNode() == null) {
                logger.debug("Adding node to root node");
                Node node = new NodeImpl(edge, lastInsertedNode, message.getTargetLifeline().getName(), message.getTargetLifeline().getPackageName());
                fillNodeWithFragments(node, message.getCombFragments());
                lastInsertedNode.addChildNode(node);
                return node;
            }

            if (lastInsertedNode.getName().equals(message.getSourceLifeline().getName()) && !lastInsertedNode.isReply()) {
                if (lastInsertedNode.getCreateEdge().getEdgeType().equals(EdgeType.SELF) && hasReply(lastInsertedNode)) {
                    logger.debug("Found self message, moving to parent");
                    return storeMessageIntoGraph(graph, message, lastInsertedNode.getParentNode());
                }

                Node node = new NodeImpl(edge, lastInsertedNode, message.getTargetLifeline().getName(), message.getTargetLifeline().getPackageName());
                fillNodeWithFragments(node, message.getCombFragments());
                lastInsertedNode.addChildNode(node);
                logger.debug("Found place for node " + node.getName() + " with message " + node.getCreateEdge().getName());
                logger.debug("Node inserted to " + lastInsertedNode.getName());
                return node;
            } else {
                return storeMessageIntoGraph(graph, message, lastInsertedNode.getParentNode());
            }
        } catch (MessageTypeException ex) {
            throw new GraphTransformationException("Exception while creating graph structure: ", ex);
        }
    }

    private Graph initializeGraphStructure(SeqDiagram diagram) {
        Node rootNode = new NodeImpl(null, null, diagram.getMessages().get(0).getSourceLifeline().getName(), diagram.getMessages().get(0).getSourceLifeline().getPackageName());
        Graph graph = new TreeGraph(rootNode, diagram);
        return graph;
    }

    private boolean hasReply(Node parentNode) {
        for (Node node : parentNode.childNodes()) {
            if (node.isReply()) {
                return true;
            }
        }
        return false;
    }

    private void fillNodeWithFragments(Node node, List<CombFragment> fragments) throws GraphTransformationException {
        for (CombFragment fragment : fragments) {
            node.addCombinedFragment(new NodeCombinedFragmentImpl(fragment, node));
        }
    }

}
