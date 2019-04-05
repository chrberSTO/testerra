/*
 * Created on 03.13.2014
 *
 * Copyright(c) 2011 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.layout.matching.detection;

import eu.tsystems.mms.tic.testframework.constants.FennecProperties;
import eu.tsystems.mms.tic.testframework.layout.matching.LayoutMatch;
import eu.tsystems.mms.tic.testframework.layout.matching.error.AmbiguousElementMovedError;
import eu.tsystems.mms.tic.testframework.layout.matching.error.LayoutFeature;
import eu.tsystems.mms.tic.testframework.layout.matching.graph.DistanceGraph;
import eu.tsystems.mms.tic.testframework.layout.matching.graph.Edge;
import eu.tsystems.mms.tic.testframework.layout.matching.graph.TemplateNode;

import java.util.LinkedList;
import java.util.List;

/**
 * User: rnhb
 * Date: 12.06.14
 * <p/>
 * Detector for errors, where an element has several wrong matches.
 */
public class AmbiguousMovementDetector extends FeatureDetector {

    public AmbiguousMovementDetector() {
        ignorePropertyKey = FennecProperties.LAYOUTCHECK_IGNORE_AMBIGUOUS_MOVEMENT;
    }

    @Override
    protected List<LayoutFeature> findFeatures(DistanceGraph distanceGraph, LayoutMatch layoutMatch) {
        List<LayoutFeature> errors = new LinkedList<LayoutFeature>();
        for (TemplateNode templateNode : distanceGraph.getTemplateNodes()) {
            List<Edge> edgesToMatchNodes = templateNode.getEdgesToMatchNodes();
            if (edgesToMatchNodes.size() > 1) {
                if (!hasEdgeToSameLocation(edgesToMatchNodes, templateNode)) {
                    errors.add(new AmbiguousElementMovedError(templateNode));
                }
            }
        }
        return errors;
    }
}
