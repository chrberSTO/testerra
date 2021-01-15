// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: framework.proto

package eu.tsystems.mms.tic.testframework.report.model;

public interface SessionContextOrBuilder extends
    // @@protoc_insertion_point(interface_extends:data.SessionContext)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.data.ContextValues context_values = 1;</code>
   * @return Whether the contextValues field is set.
   */
  boolean hasContextValues();
  /**
   * <code>.data.ContextValues context_values = 1;</code>
   * @return The contextValues.
   */
  eu.tsystems.mms.tic.testframework.report.model.ContextValues getContextValues();
  /**
   * <code>.data.ContextValues context_values = 1;</code>
   */
  eu.tsystems.mms.tic.testframework.report.model.ContextValuesOrBuilder getContextValuesOrBuilder();

  /**
   * <code>string session_key = 2;</code>
   * @return The sessionKey.
   */
  java.lang.String getSessionKey();
  /**
   * <code>string session_key = 2;</code>
   * @return The bytes for sessionKey.
   */
  com.google.protobuf.ByteString
      getSessionKeyBytes();

  /**
   * <code>string provider = 3;</code>
   * @return The provider.
   */
  java.lang.String getProvider();
  /**
   * <code>string provider = 3;</code>
   * @return The bytes for provider.
   */
  com.google.protobuf.ByteString
      getProviderBytes();

  /**
   * <code>map&lt;string, string&gt; metadata = 4;</code>
   */
  int getMetadataCount();
  /**
   * <code>map&lt;string, string&gt; metadata = 4;</code>
   */
  boolean containsMetadata(
      java.lang.String key);
  /**
   * Use {@link #getMetadataMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.String>
  getMetadata();
  /**
   * <code>map&lt;string, string&gt; metadata = 4;</code>
   */
  java.util.Map<java.lang.String, java.lang.String>
  getMetadataMap();
  /**
   * <code>map&lt;string, string&gt; metadata = 4;</code>
   */

  java.lang.String getMetadataOrDefault(
      java.lang.String key,
      java.lang.String defaultValue);
  /**
   * <code>map&lt;string, string&gt; metadata = 4;</code>
   */

  java.lang.String getMetadataOrThrow(
      java.lang.String key);

  /**
   * <code>string session_id = 6;</code>
   * @return The sessionId.
   */
  java.lang.String getSessionId();
  /**
   * <code>string session_id = 6;</code>
   * @return The bytes for sessionId.
   */
  com.google.protobuf.ByteString
      getSessionIdBytes();

  /**
   * <code>string video_id = 7;</code>
   * @return The videoId.
   */
  java.lang.String getVideoId();
  /**
   * <code>string video_id = 7;</code>
   * @return The bytes for videoId.
   */
  com.google.protobuf.ByteString
      getVideoIdBytes();
}
