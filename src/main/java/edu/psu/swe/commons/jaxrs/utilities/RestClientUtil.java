package edu.psu.swe.commons.jaxrs.utilities;

/*
 * The Pennsylvania State University © 2016
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import edu.psu.swe.commons.jaxrs.exceptions.RestClientException;

public final class RestClientUtil {

  private RestClientUtil() {

  }

  public static void checkForSuccess(Response response) throws RestClientException {
    if (!isSuccessful(response)) {
      throw new RestClientException(response);
    }
  }

  public static boolean isSuccessful(Response response) {
    boolean isSuccessful;
    Family responseFamily = response.getStatusInfo()
                                    .getFamily();
    isSuccessful = responseFamily != Family.CLIENT_ERROR && responseFamily != Family.SERVER_ERROR;

    return isSuccessful;
  }

  /**
   * Closes <code>response</code> and suppresses any known/expected exceptions
   * from closing it.
   * 
   * @param response
   */
  public static void close(Response response) {
    try {
      response.close();
    } catch (ProcessingException ignored) {
    }
  }

  /**
   * Closes <code>response</code> and passes any known/expected exceptions from
   * closing it to <code>consumer</code> (e.g. for logging).
   * 
   * @param response
   * @param consumer
   */
  public static void close(Response response, Consumer<Throwable> consumer) {
    try {
      response.close();
    } catch (ProcessingException processingException) {
      consumer.accept(processingException);
    }
  }

  /**
   * Read an entity from the response if it was found and returned.
   * 
   * @param response
   *          the {@link Response} to read from
   * @param entityType
   *          the type of entity
   * @return <code>Optional.empty()</code> if <b>Not Found</b> or empty
   *         response, otherwise <code>Optional.ofNullable(T)</code>
   * @throws RestClientException
   *           if <code>response</code> is an error response other than
   *           <code>404 Not Found</code>
   * @throws ProcessingException
   *           see {@link Response#readEntity(Class)}
   * @throws IllegalStateException
   *           see {@link Response#readEntity(Class)}
   */
  public <T> Optional<T> tryReadEntity(Response response, Class<T> entityType) throws RestClientException, ProcessingException, IllegalStateException {
    Optional<T> entity = readEntity(response, entityType, response::readEntity, Optional::ofNullable);

    return entity;
  }

  /**
   * Read an entity from the response if it was found and returned.
   * 
   * @param response
   *          the {@link Response} to read from
   * @param entityType
   *          the type of entity
   * @return <code>Optional.empty()</code> if <code>Not Found</code> or empty
   *         response, otherwise <code>Optional.ofNullable(T)</code>
   * @throws RestClientException
   *           if <code>response</code> is an error response other than
   *           <code>404 Not Found</code>
   * @throws ProcessingException
   *           see {@link Response#readEntity(GenericType)}
   * @throws IllegalStateException
   *           see {@link Response#readEntity(GenericType)}
   */
  public <T> Optional<T> tryReadEntity(Response response, GenericType<T> entityType) throws RestClientException, ProcessingException, IllegalStateException {
    Optional<T> entity = readEntity(response, entityType, response::readEntity, Optional::ofNullable);

    return entity;
  }

  /**
   * <p>
   * Read an entity from the response if it was found.
   * </p>
   * <p>
   * Useful for REST endpoints that <b>MUST</b> return an entity.
   * </p>
   * 
   * @param response
   *          the {@link Response} to read from
   * @param entityType
   *          the type of entity
   * @return <code>Optional.empty()</code> if <code>Not Found</code>, otherwise
   *         <code>Optional.of(T)</code>
   * @throws RestClientException
   *           if <code>response</code> is an error response other than
   *           <code>404 Not Found</code>
   * @throws ProcessingException
   *           see {@link Response#readEntity(Class)}
   * @throws IllegalStateException
   *           see {@link Response#readEntity(Class)}
   */
  public static <T> Optional<T> readEntity(Response response, Class<T> entityType) throws RestClientException, ProcessingException, IllegalStateException {
    Optional<T> entity = readEntity(response, entityType, response::readEntity, Optional::of);

    return entity;
  }

  /**
   * <p>
   * Read an entity from the response if it was found.
   * </p>
   * <p>
   * Useful for REST endpoints that <b>MUST</b> return an entity.
   * </p>
   * 
   * @param response
   *          the {@link Response} to read from
   * @param entityType
   *          the type of entity
   * @return <code>Optional.empty()</code> if <code>Not Found</code>, otherwise
   *         <code>Optional.of(T)</code>
   * @throws RestClientException
   *           if <code>response</code> is an error response other than
   *           <code>404 Not Found</code>
   * @throws ProcessingException
   *           see {@link Response#readEntity(GenericType)}
   * @throws IllegalStateException
   *           see {@link Response#readEntity(GenericType)}
   */
  public static <T> Optional<T> readEntity(Response response, GenericType<T> entityType) throws RestClientException, ProcessingException, IllegalStateException {
    Optional<T> entity = readEntity(response, entityType, response::readEntity, Optional::of);

    return entity;
  }

  private static <T, E> Optional<E> readEntity(Response response, T entityType, Function<T, E> readEntity, Function<E, Optional<E>> optionalOf) throws RestClientException, ProcessingException, IllegalStateException {
    Optional<E> result;

    if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
      result = Optional.empty();
    } else {
      checkForSuccess(response);

      E responseEntity = readEntity.apply(entityType);
      result = optionalOf.apply(responseEntity);
    }
    return result;
  }
}
