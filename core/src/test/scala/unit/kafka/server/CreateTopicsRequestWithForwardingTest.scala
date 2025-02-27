/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kafka.server

import org.apache.kafka.common.protocol.Errors
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

import scala.jdk.CollectionConverters._

class CreateTopicsRequestWithForwardingTest extends AbstractCreateTopicsRequestTest {

  override def enableForwarding: Boolean = true

  @ParameterizedTest
  @ValueSource(strings = Array("zk", "kraft"))
  def testForwardToController(quorum: String): Unit = {
    val req = topicsReq(Seq(topicReq("topic1")))
    val response = sendCreateTopicRequest(req, notControllerSocketServer)
    // With forwarding enabled, request could be forwarded to the active controller.
    assertEquals(Map(Errors.NONE -> 1), response.errorCounts().asScala)
  }
}
