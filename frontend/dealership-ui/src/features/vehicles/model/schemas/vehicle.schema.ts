import { z } from "zod";

import { createPageSchema } from "@/src/shared/model/page.schema";
import { formatChassis, unformatCurrency } from "@/src/shared/lib/formatters";

export const fuelTypeSchema = z.enum([
  "GASOLINA",
  "ETANOL",
  "FLEX",
  "DIESEL",
  "ELETRICO",
  "HIBRIDO",
]);

const manufactureYearSchema = z.preprocess(
  (value) => {
    if (typeof value === "string" && value.trim() === "") {
      return undefined;
    }

    return value;
  },
  z.coerce.number().int("Ano de fabricacao invalido").min(1900, "Ano de fabricacao invalido").max(2100, "Ano de fabricacao invalido")
);

const priceSchema = z.preprocess(
  (value) => {
    if (typeof value === "string") {
      return unformatCurrency(value);
    }

    return value;
  },
  z.number().positive("Preco deve ser positivo")
);

const chassisSchema = z.preprocess(
  (value) => (typeof value === "string" ? formatChassis(value) : value),
  z.string().min(5, "Chassi deve ter ao menos 5 caracteres").max(30, "Chassi deve ter no maximo 30 caracteres")
);

export const vehicleSchema = z.object({
  brand: z.string().trim().min(2, "Marca deve ter ao menos 2 caracteres"),
  model: z.string().trim().min(1, "Modelo e obrigatorio"),
  fuelType: fuelTypeSchema,
  color: z.string().trim().min(2, "Cor deve ter ao menos 2 caracteres"),
  externalColor: z.string().trim().min(2, "Cor externa deve ter ao menos 2 caracteres"),
  manufactureYear: manufactureYearSchema,
  chassis: chassisSchema,
  price: priceSchema,
  dealershipId: z.string().uuid("Concessionaria deve ser um UUID valido"),
});

export const vehicleResponseSchema = vehicleSchema.extend({
  id: z.string().uuid(),
  manufactureYear: z.number().int().nullable().optional(),
  price: z.number().nullable().optional(),
  externalColor: z.string().nullable().optional(),
  chassis: z.string().nullable().optional(),
  imageUrl: z.string().url().nullable().optional(),
});

export const vehicleImageUploadResponseSchema = z.object({
  imageUrl: z.string().url(),
});

export const pageVehicleResponseSchema = createPageSchema(vehicleResponseSchema);

export type FuelType = z.infer<typeof fuelTypeSchema>;
export type VehicleFormInput = {
  brand: string;
  model: string;
  fuelType: FuelType;
  color: string;
  externalColor: string;
  manufactureYear: string;
  chassis: string;
  price: string;
  dealershipId: string;
};
export type VehicleFormValues = z.output<typeof vehicleSchema>;
export type VehicleResponse = z.infer<typeof vehicleResponseSchema>;
export type VehicleImageUploadResponse = z.infer<typeof vehicleImageUploadResponseSchema>;
export type PageVehicleResponse = z.infer<typeof pageVehicleResponseSchema>;
