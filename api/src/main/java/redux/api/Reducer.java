package redux.api;

/**
 * A reducer accepts an accumulation and a value and returns a new accumulation. They are used to reduce a collection of
 * values down to a single value.
 *
 * @see <a href="http://redux.js.org/docs/basics/Reducers.html">http://redux.js.org/docs/basics/Reducers.html</a>
 */
public interface Reducer<S> {

	/**
	 * A pure function which returns a new state given the previous state and an action.
	 *
	 * Things you should never do inside a reducer:
	 * <ul>
	 * <li>Mutate its arguments</li>
	 * <li>Perform side effects like API calls and routing transitions</li>
	 * <li>Call non-pure functions, e.g. Date() or Random.nextInt()</li>
	 * </ul>
	 *
	 * Given the same arguments, it should calculate the next state and return it. No surprises. No side effects. No API
	 * calls. No mutations. Just a calculation.
	 *
	 * @param state The previous state
	 * @param action The dispatched action
	 * @return The new state
	 * @see <a href="http://redux.js.org/docs/basics/Reducers.html">http://redux.js.org/docs/basics/Reducers.html</a>
	 */
	S reduce(S state, Object action);

}
