import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { loadSubjects, markChapterDone, resetProgress } from "../redux/actions/subjectActions";

function SubjectsPage() {
  // useSelector reads a value OUT of the store and re-renders this
  // component whenever that value changes.
  const subjects = useSelector((state) => state.subjectState.subjects);
  const loaded = useSelector((state) => state.subjectState.loaded);

  // useDispatch gives us the function that sends actions INTO the store.
  const dispatch = useDispatch();

  // Dispatch once on mount: action -> reducer -> store -> this component.
  useEffect(() => {
    dispatch(loadSubjects());
  }, [dispatch]);

  const totalChapters = subjects.reduce((sum, s) => sum + s.totalChapters, 0);
  const doneChapters = subjects.reduce((sum, s) => sum + s.chaptersDone, 0);
  const overall = totalChapters === 0 ? 0 : Math.round((doneChapters / totalChapters) * 100);

  return (
    <div className="page">
      <h2>Subjects</h2>

      <div className="redux-summary">
        <span>
          {doneChapters} of {totalChapters} chapters covered
        </span>
        <span className="badge">{overall}% overall</span>
        <button className="danger" onClick={() => dispatch(resetProgress())}>
          Reset progress
        </button>
      </div>

      <div className="card-list">
        {!loaded && <p>Loading subjects...</p>}
        {loaded && subjects.length === 0 && <p>No subjects in subjects.json yet.</p>}

        {subjects.map((subject) => {
          const percent = Math.round((subject.chaptersDone / subject.totalChapters) * 100);
          const complete = subject.chaptersDone === subject.totalChapters;

          return (
            <div className="item-card" key={subject.id}>
              <div className="item-card-header">
                <strong>{subject.name}</strong>
                <span className="badge">{subject.code}</span>
              </div>

              <p className="subject-meta">
                {subject.faculty} &middot; {subject.credits} credits
              </p>

              <div className="progress-track">
                <div className="progress-fill" style={{ width: `${percent}%` }} />
              </div>

              <div className="item-card-footer">
                <span className={complete ? "status-completed" : "status-pending"}>
                  {subject.chaptersDone}/{subject.totalChapters} chapters
                </span>
                <button onClick={() => dispatch(markChapterDone(subject.id))} disabled={complete}>
                  {complete ? "Done" : "Mark chapter done"}
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default SubjectsPage;
