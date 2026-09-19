import subjectsData from "../../data/subjects.json";
import { LOAD_SUBJECTS, MARK_CHAPTER_DONE, RESET_PROGRESS } from "../actionTypes";

// ACTION CREATORS
// A plain function that returns an ACTION: an object with a `type`
// and (optionally) a `payload`. It does not change state itself --
// it only describes "what happened".

// Reads the local JSON file and hands it to the reducer.
export const loadSubjects = () => ({
  type: LOAD_SUBJECTS,
  payload: subjectsData,
});

// payload = which subject the user clicked
export const markChapterDone = (subjectId) => ({
  type: MARK_CHAPTER_DONE,
  payload: subjectId,
});

export const resetProgress = () => ({
  type: RESET_PROGRESS,
});
