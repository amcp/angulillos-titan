package com.bio4j.angulillos.titan;

import java.util.stream.Stream;
import java.util.Iterator;
import java.util.Optional;

import com.bio4j.angulillos.*;
import static com.bio4j.angulillos.conversions.*;
import com.thinkaurelius.titan.core.TitanVertex;
import com.thinkaurelius.titan.core.TitanEdge;
import com.thinkaurelius.titan.core.EdgeLabel;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.EdgeLabelMaker;
import com.thinkaurelius.titan.core.schema.VertexLabelMaker;

public interface TitanUntypedGraph extends UntypedGraph<TitanVertex,VertexLabel,TitanEdge,EdgeLabel> {

  TitanGraph titanGraph();

  default void commit() { titanGraph().commit(); }

  default void shutdown() { titanGraph().shutdown(); }

  @Override
  default TitanEdge addEdge(TitanVertex from, EdgeLabel edgeType, TitanVertex to) {

    return from.addEdge( edgeType, to );
  }

  @Override
  default TitanVertex addVertex(VertexLabel type) {

    return (TitanVertex) titanGraph().addVertexWithLabel(type); 
  }

  @Override
  default <V> V getPropertyV(TitanVertex vertex, String property) {

    return vertex.<V>getProperty(property);
  }

  @Override
  default <V> void setPropertyV(TitanVertex vertex, String property, V value) {

    vertex.setProperty(property, value);
  }

  @Override
  default <V> V getPropertyE(TitanEdge edge, String property) {

    return edge.<V>getProperty(property);
  }

  @Override
  default <V> void setPropertyE(TitanEdge edge, String property, V value) {

    edge.setProperty(property, value);
  }

  @Override
  default TitanVertex source(TitanEdge edge) {

    return edge.getVertex(com.tinkerpop.blueprints.Direction.OUT);
  }

  @Override
  default TitanVertex target(TitanEdge edge) {

    return edge.getVertex(com.tinkerpop.blueprints.Direction.IN);
  }

  @Override
  default Optional<Stream<TitanEdge>> out(TitanVertex vertex, EdgeLabel edgeType) {

    Iterable<TitanEdge> itb = vertex.getTitanEdges(com.tinkerpop.blueprints.Direction.OUT, edgeType);

    if ( itb.iterator().hasNext() ) {

      return Optional.of( stream( itb ) );

    } else {

      return Optional.empty();
    }
  }

  @Override
  default Optional<Stream<TitanVertex>> outV(TitanVertex vertex, EdgeLabel edgeType) {

    Iterable<TitanEdge> itb = vertex.getTitanEdges(com.tinkerpop.blueprints.Direction.OUT, edgeType);

    if ( itb.iterator().hasNext() ) {

      return Optional.of( stream( itb ).map( e -> e.getVertex(com.tinkerpop.blueprints.Direction.IN) ) );

    } else {

      return Optional.empty();
    }
  }

  @Override
  default Optional<Stream<TitanEdge>> in(TitanVertex vertex, EdgeLabel edgeType) {

    Iterable<TitanEdge> itb = vertex.getTitanEdges(com.tinkerpop.blueprints.Direction.IN, edgeType);

    if ( itb.iterator().hasNext() ) {

      return Optional.of( stream( itb ) );

    } else {

      return Optional.empty();
    }
  }

  @Override
  default Optional<Stream<TitanVertex>> inV(TitanVertex vertex, EdgeLabel edgeType) {

    Iterable<TitanEdge> itb = vertex.getTitanEdges(com.tinkerpop.blueprints.Direction.IN, edgeType);

    if ( itb.iterator().hasNext() ) {

      return Optional.of( stream( itb ).map( e -> e.getVertex(com.tinkerpop.blueprints.Direction.OUT) ) );

    } else {

      return Optional.empty();
    }
  }


  // create types
  /*
    creates a key in the graph using the provided `KeyMaker` and `name` if there is no such `TitanKey` with that `name`; otherwise it returns the existing `TitanKey` with the provided `name`.
  */
  default VertexLabel createOrGet(VertexLabelMaker labelMaker, String name) {

    VertexLabel vertexLabel = titanGraph().getVertexLabel(labelMaker.getName());

    return vertexLabel;
  }

  /*
    creates a label in the graph using the provided `LabelMaker` and `name` if there is no such `EdgeLabel` with that `name`; otherwise it returns the existing `EdgeLabel` with the provided `name`.
  */
  public default EdgeLabel createOrGet(EdgeLabelMaker labelMaker, String name) {

    Boolean isNotDefined = true;

    EdgeLabel label = null;

    // first see if there's such a thing there
    Iterator<EdgeLabel> definedLabels = titanGraph().getTypes(EdgeLabel.class).iterator();

    while( definedLabels.hasNext() ) {

      EdgeLabel someLabel = definedLabels.next();

      if ( someLabel.getName().equals(name) ) { 
        
        isNotDefined = false;
        label = someLabel;
      }
    }

    if( isNotDefined ) {

      label = labelMaker.make();
    }

    return label;
  }


  /*
    Get a `KeyMaker` already configured for creating the key corresponding to a node type. You can use this for defining the corresponding `...`.
  */
  default <
    N extends TypedVertex<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  >
  KeyMaker titanKeyMakerForVertexType(P property) {

    // note how here we take the full name so that this is scoped by the node type; see `Property`.
    KeyMaker keyMaker = titanGraph().makeKey(property.name())
      .dataType(property.valueClass())
      .indexed(com.tinkerpop.blueprints.Vertex.class)
      .unique()
	  .single();

    return keyMaker;
  }


  /*
    create a `TitanKey` for a node type, using the default configuration. If a type with the same name is present it will be returned instead.
  */
  default <
    N extends TypedVertex<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  > 
  TitanKey titanKeyForVertexType(P property) {

    return createOrGet(titanKeyMakerForVertexType(property), property.name());
  }

  /*
    Create a LabelMaker with the minimum default for a relationship type; you should use this for defining the corresponding `TitanTitanTypedEdge.Type`. This is a `LabelMaker` so that you can define any custom signature, indexing etc.
  */
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    SG extends TypedGraph<SG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel,T,TT,TG>,
    G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>
  >
  LabelMaker titanLabelMakerForEdgeType(RT relationshipType) {

    LabelMaker labelMaker = titanGraph().makeLabel(relationshipType.name())
      .directed();

    // define the arity
    switch (relationshipType.arity()) {

      case oneToOne:    labelMaker.oneToOne(); 
      case oneToMany:   labelMaker.oneToMany();
      case manyToOne:   labelMaker.manyToOne();
      case manyToMany:  labelMaker.manyToMany();
    }

    return labelMaker;
  }

  /*
    create a `EdgeLabel` for a relationship type, using the default configuration. If a type with the same name is present it will be returned instead.
  */
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    SG extends TypedGraph<SG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel,T,TT,TG>,
    G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>
  >
  EdgeLabel titanLabelForEdgeType(RT relationshipType) {

    return createOrGet(titanLabelMakerForEdgeType(relationshipType), relationshipType.name());
  }


  default <
    N extends TypedVertex<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  >
  KeyMaker titanKeyMakerForVertexProperty(P property) {

    return titanGraph().makeKey(property.name())
      // .indexed(com.tinkerpop.blueprints.Edge.class)
      .dataType(property.valueClass());
  }

  default <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    SG extends TypedGraph<SG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>, V,
    // graph
    G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>, I extends TitanUntypedGraph,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>  
  >
  KeyMaker titanKeyMakerForEdgeProperty(P property) {

    return titanGraph().makeKey(property.name())
      // .indexed(com.tinkerpop.blueprints.Edge.class)
      .dataType(property.valueClass());
  }

	default <
			// src
			S extends TypedVertex<S,ST,SG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
			ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
			SG extends TypedGraph<SG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
			// edge
			R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel,T,TT,TG>,
			RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel,T,TT,TG>,
			// property
			P extends Property<R,RT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>, V,
			// graph
			G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>, I extends TitanUntypedGraph,
			//tgt
			T extends TypedVertex<T,TT,TG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
			TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
			TG extends TypedGraph<TG,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>
			>
	TitanKey titanKeyForEdgePropertySingle(P property) {

		return createOrGet(titanKeyMakerForEdgeProperty(property).single(), property.name());
	}

	/*
		create a `TitanKey` for a single vertex property, using the default configuration. If a property with the same name is present it will be returned instead.
	  */
	default <
			N extends TypedVertex<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
			NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
			P extends Property<N,NT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>, V,
			G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,EdgeLabel>,
			I extends TitanUntypedGraph
			>
	TitanKey titanKeyForVertexPropertySingle(P property) {

		return createOrGet(titanKeyMakerForVertexProperty(property).single(), property.name());
	}

}
