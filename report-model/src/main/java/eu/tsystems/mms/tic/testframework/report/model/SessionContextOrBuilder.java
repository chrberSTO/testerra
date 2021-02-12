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
   * <pre>
   *    map&lt;string, string&gt; metadata = 4 [deprecated = true];
   * </pre>
   *
   * <code>string session_id = 6;</code>
   * @return The sessionId.
   */
  java.lang.String getSessionId();
  /**
   * <pre>
   *    map&lt;string, string&gt; metadata = 4 [deprecated = true];
   * </pre>
   *
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

  /**
   * <code>string execution_context_id = 8;</code>
   * @return The executionContextId.
   */
  java.lang.String getExecutionContextId();
  /**
   * <code>string execution_context_id = 8;</code>
   * @return The bytes for executionContextId.
   */
  com.google.protobuf.ByteString
      getExecutionContextIdBytes();

  /**
   * <code>string browser_name = 9;</code>
   * @return The browserName.
   */
  java.lang.String getBrowserName();
  /**
   * <code>string browser_name = 9;</code>
   * @return The bytes for browserName.
   */
  com.google.protobuf.ByteString
      getBrowserNameBytes();

  /**
   * <code>string browser_version = 10;</code>
   * @return The browserVersion.
   */
  java.lang.String getBrowserVersion();
  /**
   * <code>string browser_version = 10;</code>
   * @return The bytes for browserVersion.
   */
  com.google.protobuf.ByteString
      getBrowserVersionBytes();

  /**
   * <code>string capabilities = 11;</code>
   * @return The capabilities.
   */
  java.lang.String getCapabilities();
  /**
   * <code>string capabilities = 11;</code>
   * @return The bytes for capabilities.
   */
  com.google.protobuf.ByteString
      getCapabilitiesBytes();
}
