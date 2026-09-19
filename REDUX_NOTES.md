# Redux Integration — TaskFlow (Subjects module)

A minimal Redux setup added to the existing React frontend. It is deliberately
kept separate from the existing pages (Tasks, Goals, Timetable, Sessions), which
still use `useState` + axios. The Subjects page is the Redux demo.

## Why Redux at all

`useState` keeps data inside one component. If two components need the same
data, you end up passing props down through every layer in between ("prop
drilling"). Redux moves that data into one store outside the component tree, and
any component can read from it or dispatch to it directly.

## The three pieces

| Piece | File | Job |
|---|---|---|
| Store | `src/redux/store.js` | Holds the whole app state, one object |
| Action | `src/redux/actions/subjectActions.js` | Describes *what happened* |
| Reducer | `src/redux/reducers/subjectReducer.js` | Decides the *new state* |

## Data flow

```
subjects.json
     |
     v
loadSubjects()  ---->  dispatch(action)  ---->  subjectReducer(state, action)
 (ACTION CREATOR)                                       |
                                                        v
                                                   new state
                                                        |
                                                        v
                                                  STORE updated
                                                        |
                                                        v
                                       useSelector() re-renders SubjectsPage
```

One sentence: **a component dispatches an action, the reducer turns the old
state plus that action into new state, the store saves it, and every component
subscribed with `useSelector` re-renders.**

## Files added

```
src/data/subjects.json               <- the JSON data source
src/redux/actionTypes.js             <- action type constants
src/redux/actions/subjectActions.js  <- action creators
src/redux/reducers/subjectReducer.js <- the reducer (pure function)
src/redux/reducers/rootReducer.js    <- combineReducers
src/redux/store.js                   <- createStore
src/pages/SubjectsPage.js            <- reads store, dispatches actions
```

## Files modified

- `src/index.js` — wrapped `<App />` in `<Provider store={store}>`
- `src/App.js` — added `/subjects` route
- `src/components/Navbar.js` — added the Subjects link
- `src/App.css` — progress bar styles
- `package.json` — added `redux` and `react-redux`

## State shape

```js
{
  subjectState: {
    subjects: [ { id, name, code, credits, totalChapters, chaptersDone, faculty } ],
    loaded: false
  }
}
```

## Actions implemented

| Action type | Payload | Effect |
|---|---|---|
| `LOAD_SUBJECTS` | array from `subjects.json` | Fills `subjects`, sets `loaded: true` |
| `MARK_CHAPTER_DONE` | `subjectId` | Increments that subject's `chaptersDone` |
| `RESET_PROGRESS` | none | Sets every `chaptersDone` back to 0 |

## Rules the reducer follows

1. **Pure** — same input always gives the same output. No axios calls, no
   `Date.now()`, no `Math.random()` inside it.
2. **Never mutate** — `state.subjects.push(...)` is wrong. Use spread and
   `map`/`filter` to build a new array. Redux compares by reference, so a
   mutated array looks unchanged and the UI will not re-render.
3. **Always return state** — the `default` case returns `state` untouched,
   because every dispatched action reaches every reducer.

## Run it

```bash
cd frontend
npm install
npm start
```

Open http://localhost:3000/subjects. No backend needed for this page — the data
comes from the JSON file.

Install the Redux DevTools browser extension to watch each action and the state
diff it produced.

## Talking points for a viva / interview

- **Why three files instead of one `useState`?** Centralised state, predictable
  updates, and a full history of what changed — every state change has a named
  action behind it.
- **Why is the reducer pure?** So state is reproducible: replay the same actions
  from the same initial state and you get the same result. That is what makes
  time-travel debugging in DevTools possible.
- **What is `combineReducers` for?** Each reducer owns one slice of the store.
  As the app grows you add `taskState`, `goalState`, etc. without one giant
  switch statement.
- **What does `Provider` do?** It puts the store on React context so
  `useSelector` and `useDispatch` can reach it from any depth.
- **What about async (calling the Spring Boot API)?** A reducer cannot do
  async work. You need middleware — `redux-thunk` lets an action creator return
  a function that dispatches `FETCH_START`, awaits axios, then dispatches
  `FETCH_SUCCESS` or `FETCH_FAILURE`.
- **Is this how it is written today?** In a new project you would use Redux
  Toolkit (`configureStore`, `createSlice`, `createAsyncThunk`), which generates
  the action types and creators for you and allows mutating syntax through Immer.
  Classic Redux is used here because the assignment is about seeing the three
  pieces separately.
