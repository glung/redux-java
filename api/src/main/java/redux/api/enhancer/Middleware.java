package redux.api.enhancer;

import redux.api.Dispatcher;
import redux.api.Store;

/**
 * A middleware is an interface that composes a {@link Dispatcher} to return a new dispatch function. It often turns
 * async actions into actions.
 *
 * @param <S> The store type
 * @see <a href="http://redux.js.org/docs/advanced/Middleware.html">http://redux.js.org/docs/advanced/Middleware.html</a>
 */
public interface Middleware<S> {

	/**
	 * Dispatches an action. This is the only way to trigger a state change.
	 *
	 * @param store An interface to return the current state of the store.
	 * @param next The next dispatcher in the chain
	 * @param action The action
	 * @return The action
	 * @see <a href="http://redux.js.org/docs/Glossary.html#middleware">http://redux.js.org/docs/Glossary.html#middleware</a>
	 */
	Object dispatch(Store<S> store, Dispatcher next, Object action);

}
