import React from "react";
import ReactDOM from "react-dom/client";
import { Provider } from "react-redux";
import store from "./redux/store";
import "./App.css";
import App from "./App";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    {/* Provider makes the single Redux store available to every component. */}
    <Provider store={store}>
      <App />
    </Provider>
  </React.StrictMode>
);
