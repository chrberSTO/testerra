// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: framework.proto

package eu.tsystems.mms.tic.tap.model.framework;

public interface SuiteContextOrBuilder extends
    // @@protoc_insertion_point(interface_extends:data.SuiteContext)
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
  eu.tsystems.mms.tic.tap.model.framework.ContextValues getContextValues();
  /**
   * <code>.data.ContextValues context_values = 1;</code>
   */
  eu.tsystems.mms.tic.tap.model.framework.ContextValuesOrBuilder getContextValuesOrBuilder();

  /**
   * <pre>
   * list of all test
   * </pre>
   *
   * <code>repeated string test_contexts = 6;</code>
   * @return A list containing the testContexts.
   */
  java.util.List<java.lang.String>
      getTestContextsList();
  /**
   * <pre>
   * list of all test
   * </pre>
   *
   * <code>repeated string test_contexts = 6;</code>
   * @return The count of testContexts.
   */
  int getTestContextsCount();
  /**
   * <pre>
   * list of all test
   * </pre>
   *
   * <code>repeated string test_contexts = 6;</code>
   * @param index The index of the element to return.
   * @return The testContexts at the given index.
   */
  java.lang.String getTestContexts(int index);
  /**
   * <pre>
   * list of all test
   * </pre>
   *
   * <code>repeated string test_contexts = 6;</code>
   * @param index The index of the value to return.
   * @return The bytes of the testContexts at the given index.
   */
  com.google.protobuf.ByteString
      getTestContextsBytes(int index);

  /**
   * <pre>
   * reference
   * </pre>
   *
   * <code>string execution_context_id = 7;</code>
   * @return The executionContextId.
   */
  java.lang.String getExecutionContextId();
  /**
   * <pre>
   * reference
   * </pre>
   *
   * <code>string execution_context_id = 7;</code>
   * @return The bytes for executionContextId.
   */
  com.google.protobuf.ByteString
      getExecutionContextIdBytes();
}
