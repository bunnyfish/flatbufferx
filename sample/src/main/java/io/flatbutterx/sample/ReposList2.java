// automatically generated by the FlatBuffers compiler, do not modify

package io.flatbutterx.sample;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class ReposList2 extends Table {
  public static ReposList2 getRootAsReposList2(ByteBuffer _bb) { return getRootAsReposList2(_bb, new ReposList2()); }
  public static ReposList2 getRootAsReposList2(ByteBuffer _bb, ReposList2 obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public ReposList2 __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public Repo2 repos(int j) { return repos(new Repo2(), j); }
  public Repo2 repos(Repo2 obj, int j) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int reposLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }

  public static int createReposList2(FlatBufferBuilder builder,
      int reposOffset) {
    builder.startObject(1);
    ReposList2.addRepos(builder, reposOffset);
    return ReposList2.endReposList2(builder);
  }

  public static void startReposList2(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addRepos(FlatBufferBuilder builder, int reposOffset) { builder.addOffset(0, reposOffset, 0); }
  public static int createReposVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startReposVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endReposList2(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishReposList2Buffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
  public static void finishSizePrefixedReposList2Buffer(FlatBufferBuilder builder, int offset) { builder.finishSizePrefixed(offset); }
}
