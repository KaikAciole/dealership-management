import type { AxiosError } from "axios";

type ErrorPayload = {
  message?: string;
  errors?: string[];
};

export function getHttpErrorMessage(error: unknown): string {
  const fallback = "Nao foi possivel concluir a operacao. Tente novamente.";

  const axiosError = error as AxiosError<ErrorPayload>;
  if (!axiosError?.isAxiosError) {
    return fallback;
  }

  const payload = axiosError.response?.data;
  if (payload?.errors?.length) {
    return payload.errors.join(" | ");
  }

  if (payload?.message) {
    return payload.message;
  }

  if (axiosError.message) {
    return axiosError.message;
  }

  return fallback;
}
