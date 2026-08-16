import axios from "axios";
import { toast } from "sonner";

import { env } from "@/src/shared/config/env";
import { getHttpErrorMessage } from "@/src/shared/lib/http-error";

export const api = axios.create({
  baseURL: env.apiBaseUrl,
  timeout: 15_000,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.response.use(
  (response) => response,
  (error: unknown) => {
    const message = getHttpErrorMessage(error);

    if (typeof window !== "undefined") {
      toast.error(message);
    }

    return Promise.reject(error);
  }
);
