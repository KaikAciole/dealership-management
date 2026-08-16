import { z } from "zod";

import { isValidCnpj } from "@/src/shared/lib/validators/cnpj";
import { normalizeDigits } from "@/src/shared/lib/formatters";
import { createPageSchema } from "@/src/shared/model/page.schema";

const cepRegex = /^\d{5}-?\d{3}$/;

const cnpjSchema = z.preprocess(
  (value) => (typeof value === "string" ? normalizeDigits(value) : value),
  z
    .string()
    .length(14, "CNPJ deve possuir 14 digitos")
    .refine((value) => isValidCnpj(value), "CNPJ invalido")
);

const cepSchema = z.preprocess(
  (value) => (typeof value === "string" ? normalizeDigits(value) : value),
  z.string().regex(cepRegex, "CEP invalido")
);

export const dealershipAddressSchema = z.object({
  cep: cepSchema,
  street: z.string().trim().min(3, "Rua deve ter ao menos 3 caracteres"),
  number: z.string().trim().min(1, "Numero obrigatorio"),
  neighborhood: z.string().trim().min(2, "Bairro deve ter ao menos 2 caracteres"),
  city: z.string().trim().min(2, "Cidade deve ter ao menos 2 caracteres"),
  state: z
    .preprocess((value) => (typeof value === "string" ? value.trim().toUpperCase() : value), z.string().length(2, "UF deve possuir 2 caracteres")),
});

export const dealershipSchema = z.object({
  corporateName: z
    .string()
    .trim()
    .min(3, "Razao social deve ter ao menos 3 caracteres"),
  cnpj: cnpjSchema,
  address: dealershipAddressSchema,
});

const dealershipAddressResponseSchema = z.object({
  cep: z.string(),
  street: z.string().nullable().optional(),
  number: z.string().nullable().optional(),
  neighborhood: z.string().nullable().optional(),
  city: z.string().nullable().optional(),
  state: z.string().nullable().optional(),
});

export const dealershipResponseSchema = z.object({
  id: z.string().uuid(),
  corporateName: z.string(),
  cnpj: z.string(),
  address: dealershipAddressResponseSchema,
  foundationDate: z.string().optional().nullable(),
  isActive: z.boolean().optional().nullable(),
});

export const pageDealershipResponseSchema = createPageSchema(dealershipResponseSchema);

export type DealershipFormValues = z.infer<typeof dealershipSchema>;
export type DealershipAddressFormValues = z.infer<typeof dealershipAddressSchema>;
export type DealershipResponse = z.infer<typeof dealershipResponseSchema>;
export type PageDealershipResponse = z.infer<typeof pageDealershipResponseSchema>;
