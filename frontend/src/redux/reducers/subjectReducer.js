import { LOAD_SUBJECTS, MARK_CHAPTER_DONE, RESET_PROGRESS } from "../actionTypes";

// The slice of the store this reducer owns.
const initialState = {
  subjects: [],
  loaded: false,
};

// REDUCER
// (currentState, action) => newState
// Pure function: no API calls, no mutation, no Math.random(), no Date.now().
// Always return a NEW object/array instead of editing the old one.
export default function subjectReducer(state = initialState, action) {
  switch (action.type) {
    case LOAD_SUBJECTS:
      return {
        ...state,
        subjects: action.payload,
        loaded: true,
      };

    case MARK_CHAPTER_DONE:
      return {
        ...state,
        subjects: state.subjects.map((subject) =>
          subject.id === action.payload && subject.chaptersDone < subject.totalChapters
            ? { ...subject, chaptersDone: subject.chaptersDone + 1 }
            : subject
        ),
      };

    case RESET_PROGRESS:
      return {
        ...state,
        subjects: state.subjects.map((subject) => ({ ...subject, chaptersDone: 0 })),
      };

    // Unknown action -> return the state untouched.
    default:
      return state;
  }
}
