import { z } from "zod";

export const viacepSchema = z.object({
  cep: z.string().optional(),
  logradouro: z.string().optional(),
  bairro: z.string().optional(),
  localidade: z.string().optional(),
  uf: z.string().optional(),
  erro: z.boolean().optional(),
});

export type ViaCepResponse = z.infer<typeof viacepSchema>;
