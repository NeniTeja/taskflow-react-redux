import { createStore } from "redux";
import rootReducer from "./reducers/rootReducer";

// STORE
// One single object that holds the entire app state.
// Created once, at app startup, from the root reducer.
const store = createStore(
  rootReducer,
  // Optional: enables the Redux DevTools browser extension.
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
);

export default store;
