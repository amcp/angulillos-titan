package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.*;

import static com.bio4j.angulillos.conversions.*;

import com.thinkaurelius.titan.core.attribute.Cmp;
import com.thinkaurelius.titan.core.*;

import java.util.stream.Stream;
import java.util.Optional;
import java.util.Iterator;
import com.tinkerpop.blueprints.Edge;

public interface TitanTypedEdgeIndex <
  // src
  S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
  ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
  SG extends TypedGraph<SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
  // edge
  R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>, 
  RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>,
  // property
  P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, V,
  G extends TypedGraph<G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
  //tgt
  T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
  TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
  TG extends TypedGraph<TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
  I extends TitanUntypedGraph
> 
extends 
  TypedEdgeIndex<
    S,ST,SG,
    R,RT, P,V, G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,
    T,TT,TG
  >
{

  public static interface Unique <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>, 
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  >
  extends 
    TypedEdgeIndex.Unique<
      S,ST,SG,
      R,RT, P,V, G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,
      T,TT,TG
    > 
  {}

  public static interface List <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>, 
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  > 
  extends 
    TypedEdgeIndex.List<
      S,ST,SG,
      R,RT, P,V, G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,
      T,TT,TG
    >
  {}

  public static abstract class Default <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>, 
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  >
  implements
    TitanTypedEdgeIndex<
      S,ST,SG,
      R,RT, P,V,G,
      T,TT,TG,I
    >
  {

    public Default(G graph, P property) {

      this.graph = graph;
      this.property = property;
    }

    protected G graph;
    protected P property;

    @Override
    public G graph() { return graph; }

    @Override public Stream<R> query(com.tinkerpop.blueprints.Compare predicate, V value) {

      RT elmt = property.elementType();

      Stream<R> strm = stream( graph().raw().titanGraph()
        .query().has(
          property.name(),
          predicate,
          value
        )
        .edges()
      )
      .map( e -> elmt.from( (TitanEdge) e ) );

      return strm;


      // java.util.List<R> list = new LinkedList<>();

      // Iterator<Edge> iterator = graph().raw().titanGraph()
      //   .query().has(
      //     property.name(),
      //     predicate,
      //     value
      //   )
      //   .edges().iterator();

      // Boolean someResult = iterator.hasNext();

      // while ( iterator.hasNext() ) {

      //   list.add(property.elementType().from( (TitanEdge) iterator.next() ));
      // }

      // if (someResult ) {

      //   return Optional.of(list);
      // } else {

      //   return Optional.empty();
      // }
    }
  }


  /* Default implementation of a relationship unique index */
  public final class DefaultUnique <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>, 
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  > 
  extends
    Default<
      S,ST,SG,
      R,RT, P,V, G,
      T,TT,TG,I
    >
  implements 
    Unique<
      S,ST,SG,
      R,RT, P,V, G,
      T,TT,TG,I
    > 
  {

    public DefaultUnique(G graph, P property) {

      super(graph,property);
    }

    // public Optional<R> getRelationship(V byValue) {

    //   // crappy Java generics force the cast here
    //   TitanEdge uglyStuff = (TitanEdge) graph().raw().titanGraph()
    //     .query().has(
    //       property.name(),
    //       Cmp.EQUAL, 
    //       byValue
    //     )
    //     .edges().iterator().next();

    //   return Optional.of( property.elementType().from(uglyStuff) );
    // }

  }

  final class DefaultList <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>, 
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  >
  extends
    Default<
      S,ST,SG,
      R,RT, P,V, G,
      T,TT,TG,I
    > 
  implements 
    List<
      S,ST,SG,
      R,RT, P,V, G,
      T,TT,TG,I
    >
  {

    public DefaultList(G graph, P property) {

      super(graph,property);
    }
  }

}