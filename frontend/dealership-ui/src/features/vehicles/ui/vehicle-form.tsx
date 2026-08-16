"use client";

import Image from "next/image";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect, useMemo, useState } from "react";
import { type Resolver, useForm } from "react-hook-form";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useCreateVehicle, useUpdateVehicle, useUploadVehicleImage } from "@/src/features/vehicles/hooks/use-vehicles";
import {
  fuelTypeSchema,
  vehicleSchema,
  type VehicleFormInput,
  type VehicleResponse,
} from "@/src/features/vehicles/model/schemas/vehicle.schema";
import { type DealershipResponse } from "@/src/features/dealerships/model/schemas/dealership.schema";
import { formatChassis, formatCurrency } from "@/src/shared/lib/formatters";

type VehicleFormProps = {
  dealerships: DealershipResponse[];
  initialData?: VehicleResponse;
  onSuccess?: (vehicle: VehicleResponse) => void;
};

const baseDefaultValues: VehicleFormInput = {
  brand: "",
  model: "",
  fuelType: "FLEX",
  color: "",
  externalColor: "",
  manufactureYear: "",
  chassis: "",
  price: "",
  dealershipId: "",
};

function toDefaultValues(initialData?: VehicleResponse): VehicleFormInput {
  if (!initialData) {
    return baseDefaultValues;
  }

  return {
    brand: initialData.brand,
    model: initialData.model,
    fuelType: initialData.fuelType,
    color: initialData.color,
    externalColor: initialData.externalColor ?? "",
    manufactureYear: String(initialData.manufactureYear ?? ""),
    chassis: initialData.chassis ?? "",
    price: formatCurrency(initialData.price ?? undefined),
    dealershipId: initialData.dealershipId,
  };
}

function FieldError({ message }: { message?: string }) {
  if (!message) {
    return null;
  }

  return <p className="text-xs text-destructive">{message}</p>;
}

export function VehicleForm({ dealerships, initialData, onSuccess }: VehicleFormProps) {
  const createVehicleMutation = useCreateVehicle();
  const updateVehicleMutation = useUpdateVehicle();
  const uploadVehicleImageMutation = useUploadVehicleImage();
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [uploadedImageUrl, setUploadedImageUrl] = useState<string | null>(null);
  const isEditing = Boolean(initialData?.id);
  const initialImageUrl = useMemo(() => initialData?.imageUrl ?? null, [initialData?.imageUrl]);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<VehicleFormInput>({
    resolver: zodResolver(vehicleSchema) as unknown as Resolver<VehicleFormInput>,
    defaultValues: toDefaultValues(initialData),
  });

  useEffect(() => {
    reset(toDefaultValues(initialData));
  }, [initialData, reset]);

  const onSubmit = handleSubmit(async (values) => {
    const payload = vehicleSchema.parse({
      ...values,
      manufactureYear: values.manufactureYear,
      price: values.price,
      chassis: values.chassis,
    });

    const vehicle = isEditing && initialData?.id
      ? await updateVehicleMutation.mutateAsync({ id: initialData.id, payload })
      : await createVehicleMutation.mutateAsync(payload);

    let vehicleWithImage = vehicle;

    if (selectedFile) {
      try {
        vehicleWithImage = await uploadVehicleImageMutation.mutateAsync({
          vehicleId: vehicle.id,
          file: selectedFile,
        });
      } catch {
        toast.warning(
          "O veículo foi salvo, mas a imagem não pôde ser enviada. Você pode cadastrar e editar normalmente enquanto o bucket estiver indisponível."
        );
      }
    }

    toast.success(isEditing ? "Veiculo atualizado com sucesso." : "Veiculo cadastrado com sucesso.");
    setSelectedFile(null);
    setUploadedImageUrl(vehicleWithImage.imageUrl ?? null);
    onSuccess?.(vehicleWithImage);
  });

  const isSubmitting =
    createVehicleMutation.isPending ||
    updateVehicleMutation.isPending ||
    uploadVehicleImageMutation.isPending;

  return (
    <Card className="border-slate-200 bg-white/90 shadow-lg shadow-slate-200/60 backdrop-blur">
      <CardHeader>
        <CardTitle>{isEditing ? "Editar veiculo" : "Novo veiculo"}</CardTitle>
        <CardDescription>
          Os campos mascarados sao higienizados antes do envio e o payload final respeita o DTO da API.
        </CardDescription>
      </CardHeader>

      <CardContent>
        <form className="grid gap-4" onSubmit={onSubmit}>
          <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
            <div className="grid gap-2">
              <Label htmlFor="brand">Marca</Label>
              <Input id="brand" {...register("brand")} />
              <FieldError message={errors.brand?.message} />
            </div>

            <div className="grid gap-2">
              <Label htmlFor="model">Modelo</Label>
              <Input id="model" {...register("model")} />
              <FieldError message={errors.model?.message} />
            </div>
          </div>

          <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
            <div className="grid gap-2">
              <Label htmlFor="fuelType">Combustivel</Label>
              <select
                id="fuelType"
                className="h-10 rounded-md border border-input bg-background px-3 py-2 text-sm"
                {...register("fuelType")}
              >
                {fuelTypeSchema.options.map((fuel) => (
                  <option key={fuel} value={fuel}>
                    {fuel}
                  </option>
                ))}
              </select>
              <FieldError message={errors.fuelType?.message} />
            </div>

            <div className="grid gap-2">
              <Label htmlFor="color">Cor</Label>
              <Input id="color" {...register("color")} />
              <FieldError message={errors.color?.message} />
            </div>
          </div>

          <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
            <div className="grid gap-2">
              <Label htmlFor="externalColor">Cor externa</Label>
              <Input id="externalColor" {...register("externalColor")} />
              <FieldError message={errors.externalColor?.message} />
            </div>

            <div className="grid gap-2">
              <Label htmlFor="manufactureYear">Ano de fabricacao</Label>
              <Input id="manufactureYear" type="number" {...register("manufactureYear")} />
              <FieldError message={errors.manufactureYear?.message} />
            </div>

            <div className="grid gap-2">
              <Label htmlFor="price">Preco</Label>
              <Input
                id="price"
                inputMode="numeric"
                placeholder="R$ 0,00"
                {...register("price", {
                  onChange: (event) => {
                    event.target.value = formatCurrency(event.target.value);
                  },
                })}
              />
              <FieldError message={errors.price?.message} />
            </div>
          </div>

          <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
            <div className="grid gap-2">
              <Label htmlFor="chassis">Chassi</Label>
              <Input
                id="chassis"
                placeholder="Ex: 9BW..."
                {...register("chassis", {
                  onChange: (event) => {
                    event.target.value = formatChassis(event.target.value);
                  },
                })}
              />
              <FieldError message={errors.chassis?.message} />
            </div>

            <div className="grid gap-2">
              <Label htmlFor="dealershipId">Concessionaria</Label>
              <select
                id="dealershipId"
                className="h-10 rounded-md border border-input bg-background px-3 py-2 text-sm"
                {...register("dealershipId")}
              >
                <option value="">Selecione uma concessionaria</option>
                {dealerships.map((dealership) => (
                  <option key={dealership.id} value={dealership.id}>
                    {dealership.corporateName}
                  </option>
                ))}
              </select>
              <FieldError message={errors.dealershipId?.message} />
            </div>
          </div>

          <div className="grid gap-2">
            <Label htmlFor="vehicleImage">Foto do veiculo</Label>
            <Input
              id="vehicleImage"
              type="file"
              accept="image/*"
              onChange={(event) => {
                const file = event.target.files?.[0] ?? null;
                setSelectedFile(file);
              }}
            />
          </div>

          <Button
            type="submit"
            className="bg-gradient-to-r from-sky-600 to-blue-700 text-white hover:from-sky-700 hover:to-blue-800"
            disabled={isSubmitting}
          >
            {isSubmitting
              ? "Salvando..."
              : isEditing
                ? "Atualizar veiculo"
                : "Cadastrar veiculo"}
          </Button>
        </form>

        {uploadedImageUrl && (
          <div className="mt-6 grid gap-2">
            <p className="text-sm font-medium">Imagem do veiculo</p>
            <Image
              src={uploadedImageUrl}
              alt="Imagem do veiculo"
              width={360}
              height={220}
              className="h-auto w-full max-w-sm rounded-lg border border-border object-cover"
              unoptimized
            />
          </div>
        )}

        {!uploadedImageUrl && initialImageUrl && (
          <div className="mt-6 grid gap-2">
            <p className="text-sm font-medium">Imagem do veiculo</p>
            <Image
              src={initialImageUrl}
              alt="Imagem do veiculo"
              width={360}
              height={220}
              className="h-auto w-full max-w-sm rounded-lg border border-border object-cover"
              unoptimized
            />
          </div>
        )}
      </CardContent>
    </Card>
  );
}
