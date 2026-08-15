import { viacepSchema } from "@/src/features/dealerships/model/schemas/viacep.schema";

export async function fetchAddressByCep(cep: string) {
  const normalizedCep = cep.replace(/\D/g, "");
  const response = await fetch(`https://viacep.com.br/ws/${normalizedCep}/json/`);

  if (!response.ok) {
    throw new Error("Falha ao consultar o CEP informado.");
  }

  const parsed = viacepSchema.parse(await response.json());
  if (parsed.erro) {
    throw new Error("CEP nao encontrado.");
  }

  return parsed;
}