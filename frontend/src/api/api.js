import axios from "axios";

// Single axios instance pointed at the Spring Boot backend.
const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

export default api;
