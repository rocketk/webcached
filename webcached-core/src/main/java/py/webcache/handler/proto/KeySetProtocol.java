// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: KeySet.proto

package py.webcache.handler.proto;

public final class KeySetProtocol {
  private KeySetProtocol() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface KeySetProtoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // repeated string keys = 1;
    /**
     * <code>repeated string keys = 1;</code>
     */
    java.util.List<String>
    getKeysList();
    /**
     * <code>repeated string keys = 1;</code>
     */
    int getKeysCount();
    /**
     * <code>repeated string keys = 1;</code>
     */
    String getKeys(int index);
    /**
     * <code>repeated string keys = 1;</code>
     */
    com.google.protobuf.ByteString
        getKeysBytes(int index);
  }
  /**
   * Protobuf type {@code com.xinhuanet.csis.common.webcache.handler.proto.KeySetProto}
   */
  public static final class KeySetProto extends
      com.google.protobuf.GeneratedMessage
      implements KeySetProtoOrBuilder {
    // Use KeySetProto.newBuilder() to construct.
    private KeySetProto(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private KeySetProto(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final KeySetProto defaultInstance;
    public static KeySetProto getDefaultInstance() {
      return defaultInstance;
    }

    public KeySetProto getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private KeySetProto(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                keys_ = new com.google.protobuf.LazyStringArrayList();
                mutable_bitField0_ |= 0x00000001;
              }
              keys_.add(input.readBytes());
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          keys_ = new com.google.protobuf.UnmodifiableLazyStringList(keys_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return KeySetProtocol.internal_static_com_xinhuanet_csis_common_webcache_handler_proto_KeySetProto_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return KeySetProtocol.internal_static_com_xinhuanet_csis_common_webcache_handler_proto_KeySetProto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              KeySetProto.class, Builder.class);
    }

    public static com.google.protobuf.Parser<KeySetProto> PARSER =
        new com.google.protobuf.AbstractParser<KeySetProto>() {
      public KeySetProto parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new KeySetProto(input, extensionRegistry);
      }
    };

    @Override
    public com.google.protobuf.Parser<KeySetProto> getParserForType() {
      return PARSER;
    }

    // repeated string keys = 1;
    public static final int KEYS_FIELD_NUMBER = 1;
    private com.google.protobuf.LazyStringList keys_;
    /**
     * <code>repeated string keys = 1;</code>
     */
    public java.util.List<String>
        getKeysList() {
      return keys_;
    }
    /**
     * <code>repeated string keys = 1;</code>
     */
    public int getKeysCount() {
      return keys_.size();
    }
    /**
     * <code>repeated string keys = 1;</code>
     */
    public String getKeys(int index) {
      return keys_.get(index);
    }
    /**
     * <code>repeated string keys = 1;</code>
     */
    public com.google.protobuf.ByteString
        getKeysBytes(int index) {
      return keys_.getByteString(index);
    }

    private void initFields() {
      keys_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      for (int i = 0; i < keys_.size(); i++) {
        output.writeBytes(1, keys_.getByteString(i));
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      {
        int dataSize = 0;
        for (int i = 0; i < keys_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeBytesSizeNoTag(keys_.getByteString(i));
        }
        size += dataSize;
        size += 1 * getKeysList().size();
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @Override
    protected Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static KeySetProto parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static KeySetProto parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static KeySetProto parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static KeySetProto parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static KeySetProto parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static KeySetProto parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static KeySetProto parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static KeySetProto parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static KeySetProto parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static KeySetProto parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(KeySetProto prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code com.xinhuanet.csis.common.webcache.handler.proto.KeySetProto}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements KeySetProtoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return KeySetProtocol.internal_static_com_xinhuanet_csis_common_webcache_handler_proto_KeySetProto_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return KeySetProtocol.internal_static_com_xinhuanet_csis_common_webcache_handler_proto_KeySetProto_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                KeySetProto.class, Builder.class);
      }

      // Construct using com.xinhuanet.csis.common.webcache.handler.proto.KeySetProtocol.KeySetProto.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        keys_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return KeySetProtocol.internal_static_com_xinhuanet_csis_common_webcache_handler_proto_KeySetProto_descriptor;
      }

      public KeySetProto getDefaultInstanceForType() {
        return KeySetProto.getDefaultInstance();
      }

      public KeySetProto build() {
        KeySetProto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public KeySetProto buildPartial() {
        KeySetProto result = new KeySetProto(this);
        int from_bitField0_ = bitField0_;
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          keys_ = new com.google.protobuf.UnmodifiableLazyStringList(
              keys_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.keys_ = keys_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof KeySetProto) {
          return mergeFrom((KeySetProto)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(KeySetProto other) {
        if (other == KeySetProto.getDefaultInstance()) return this;
        if (!other.keys_.isEmpty()) {
          if (keys_.isEmpty()) {
            keys_ = other.keys_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureKeysIsMutable();
            keys_.addAll(other.keys_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        KeySetProto parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (KeySetProto) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // repeated string keys = 1;
      private com.google.protobuf.LazyStringList keys_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureKeysIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          keys_ = new com.google.protobuf.LazyStringArrayList(keys_);
          bitField0_ |= 0x00000001;
         }
      }
      /**
       * <code>repeated string keys = 1;</code>
       */
      public java.util.List<String>
          getKeysList() {
        return java.util.Collections.unmodifiableList(keys_);
      }
      /**
       * <code>repeated string keys = 1;</code>
       */
      public int getKeysCount() {
        return keys_.size();
      }
      /**
       * <code>repeated string keys = 1;</code>
       */
      public String getKeys(int index) {
        return keys_.get(index);
      }
      /**
       * <code>repeated string keys = 1;</code>
       */
      public com.google.protobuf.ByteString
          getKeysBytes(int index) {
        return keys_.getByteString(index);
      }
      /**
       * <code>repeated string keys = 1;</code>
       */
      public Builder setKeys(
          int index, String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureKeysIsMutable();
        keys_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated string keys = 1;</code>
       */
      public Builder addKeys(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureKeysIsMutable();
        keys_.add(value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated string keys = 1;</code>
       */
      public Builder addAllKeys(
          Iterable<String> values) {
        ensureKeysIsMutable();
        super.addAll(values, keys_);
        onChanged();
        return this;
      }
      /**
       * <code>repeated string keys = 1;</code>
       */
      public Builder clearKeys() {
        keys_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      /**
       * <code>repeated string keys = 1;</code>
       */
      public Builder addKeysBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureKeysIsMutable();
        keys_.add(value);
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:com.xinhuanet.csis.common.webcache.handler.proto.KeySetProto)
    }

    static {
      defaultInstance = new KeySetProto(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:com.xinhuanet.csis.common.webcache.handler.proto.KeySetProto)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_com_xinhuanet_csis_common_webcache_handler_proto_KeySetProto_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_com_xinhuanet_csis_common_webcache_handler_proto_KeySetProto_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\014KeySet.proto\0220com.xinhuanet.csis.commo" +
      "n.webcache.handler.proto\"\033\n\013KeySetProto\022" +
      "\014\n\004keys\030\001 \003(\tBB\n0com.xinhuanet.csis.comm" +
      "on.webcache.handler.protoB\016KeySetProtoco" +
      "l"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_com_xinhuanet_csis_common_webcache_handler_proto_KeySetProto_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_com_xinhuanet_csis_common_webcache_handler_proto_KeySetProto_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_com_xinhuanet_csis_common_webcache_handler_proto_KeySetProto_descriptor,
              new String[] { "Keys", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}