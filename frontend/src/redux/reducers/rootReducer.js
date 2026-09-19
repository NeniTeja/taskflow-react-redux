import { combineReducers } from "redux";
import subjectReducer from "./subjectReducer";

// combineReducers builds the shape of the whole store:
// state = { subjectState: { subjects: [], loaded: false } }
// Add more slices here later, e.g. taskState, goalState.
const rootReducer = combineReducers({
  subjectState: subjectReducer,
});

export default rootReducer;
