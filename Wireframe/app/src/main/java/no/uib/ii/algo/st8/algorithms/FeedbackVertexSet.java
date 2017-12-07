package no.uib.ii.algo.st8.algorithms;

import static no.uib.ii.algo.st8.algorithms.GirthInspector.isAcyclic;

import java.util.Collection;
import java.util.HashSet;

import no.uib.ii.algo.st8.util.InducedSubgraph;
import no.uib.ii.algo.st8.util.PowersetIterator;

import org.jgrapht.graph.SimpleGraph;

public class FeedbackVertexSet<V, E> extends Algorithm<V, E, Collection<V>> {

	public FeedbackVertexSet(SimpleGraph<V, E> graph) {
		super(graph);
	}

	public Collection<V> execute() {
		if (isAcyclic(graph))
			return new HashSet<V>();

		PowersetIterator<V> subsets = new PowersetIterator<V>(graph.vertexSet());

		Collection<V> vertices = new HashSet<V>(graph.vertexSet().size());
		Collection<V> fvs = new HashSet<V>(graph.vertexSet().size());

		Collection<V> currentBestFvs = graph.vertexSet();

		while (subsets.hasNext()) {
			fvs = subsets.next();

			if (fvs.size() >= currentBestFvs.size())
				continue;

			progress(fvs.size(), graph.vertexSet().size());
			if (cancelFlag)
				return null;

			vertices.clear();
			vertices.addAll(graph.vertexSet());
			vertices.removeAll(fvs);

			SimpleGraph<V, E> h = InducedSubgraph.inducedSubgraphOf(graph,
					vertices);

			if (isAcyclic(h))
				currentBestFvs = fvs;
		}
		return currentBestFvs;
	}
}
