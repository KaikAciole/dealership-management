"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect } from "react";
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
import { fetchAddressByCep } from "@/src/features/dealerships/api/viacep.service";
import { useCreateDealership, useUpdateDealership } from "@/src/features/dealerships/hooks/use-dealerships";
import {
  dealershipSchema,
  type DealershipResponse,
} from "@/src/features/dealerships/model/schemas/dealership.schema";
import { formatCep, formatCnpj, normalizeDigits } from "@/src/shared/lib/formatters";

type DealershipFormInput = {
  corporateName: string;
  cnpj: string;
  address: {
    cep: string;
    street: string;
    number: string;
    neighborhood: string;
    city: string;
    state: string;
  };
};

type DealershipFormProps = {
  initialData?: DealershipResponse;
  onSuccess?: (dealership: DealershipResponse) => void;
};

const baseDefaultValues: DealershipFormInput = {
  corporateName: "",
  cnpj: "",
  address: {
    cep: "",
    street: "",
    number: "",
    neighborhood: "",
    city: "",
    state: "",
  },
};

function toDefaultValues(initialData?: DealershipResponse): DealershipFormInput {
  if (!initialData) {
    return baseDefaultValues;
  }

  return {
    corporateName: initialData.corporateName,
    cnpj: formatCnpj(initialData.cnpj),
    address: {
      cep: formatCep(initialData.address.cep),
      street: initialData.address.street,
      number: initialData.address.number,
      neighborhood: initialData.address.neighborhood,
      city: initialData.address.city,
      state: initialData.address.state,
    },
  };
}

function FieldError({ message }: { message?: string }) {
  if (!message) {
    return null;
  }

  return <p className="text-xs text-destructive">{message}</p>;
}

export function DealershipForm({ initialData, onSuccess }: DealershipFormProps) {
  const createDealershipMutation = useCreateDealership();
  const updateDealershipMutation = useUpdateDealership();
  const isEditing = Boolean(initialData?.id);

  const {
    register,
    handleSubmit,
    setValue,
    getValues,
    reset,
    formState: { errors },
  } = useForm<DealershipFormInput>({
    resolver: zodResolver(dealershipSchema) as unknown as Resolver<DealershipFormInput>,
    defaultValues: toDefaultValues(initialData),
  });

  useEffect(() => {
    reset(toDefaultValues(initialData));
  }, [initialData, reset]);

  async function handleCepBlur() {
    const cep = normalizeDigits(getValues("address.cep"));

    if (cep.length !== 8) {
      return;
    }

    try {
      const result = await fetchAddressByCep(cep);
      setValue("address.street", result.logradouro ?? "", { shouldValidate: true });
      setValue("address.neighborhood", result.bairro ?? "", { shouldValidate: true });
      setValue("address.city", result.localidade ?? "", { shouldValidate: true });
      setValue("address.state", (result.uf ?? "").toUpperCase(), { shouldValidate: true });
    } catch (error) {
      const message = error instanceof Error ? error.message : "Falha ao buscar CEP.";
      toast.warning(message);
    }
  }

  const onSubmit = handleSubmit(async (values) => {
    const payload = dealershipSchema.parse(values);
    const result = isEditing && initialData?.id
      ? await updateDealershipMutation.mutateAsync({ id: initialData.id, payload })
      : await createDealershipMutation.mutateAsync(payload);

    toast.success(isEditing ? "Concessionaria atualizada com sucesso." : "Concessionaria cadastrada com sucesso.");
    onSuccess?.(result);
  });

  const isPending = createDealershipMutation.isPending || updateDealershipMutation.isPending;

  return (
    <Card>
      <CardHeader>
        <CardTitle>{isEditing ? "Editar concessionaria" : "Nova concessionaria"}</CardTitle>
        <CardDescription>
          CNPJ e CEP sao higienizados antes da validacao. O payload final sai limpo para a API.
        </CardDescription>
      </CardHeader>
      <CardContent>
        <form className="grid gap-4" onSubmit={onSubmit}>
          <div className="grid gap-2">
            <Label htmlFor="corporateName">Razao social</Label>
            <Input id="corporateName" {...register("corporateName")} />
            <FieldError message={errors.corporateName?.message} />
          </div>

          <div className="grid gap-2">
            <Label htmlFor="cnpj">CNPJ</Label>
            <Input
              id="cnpj"
              inputMode="numeric"
              placeholder="00.000.000/0000-00"
              {...register("cnpj", {
                onChange: (event) => {
                  event.target.value = formatCnpj(event.target.value);
                },
              })}
            />
            <FieldError message={errors.cnpj?.message} />
          </div>

          <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
            <div className="grid gap-2">
              <Label htmlFor="address.cep">CEP</Label>
              <Input
                id="address.cep"
                inputMode="numeric"
                placeholder="00000-000"
                {...register("address.cep", {
                  onBlur: async () => {
                    await handleCepBlur();
                  },
                  onChange: (event) => {
                    event.target.value = formatCep(event.target.value);
                  },
                })}
              />
              <FieldError message={errors.address?.cep?.message} />
            </div>

            <div className="grid gap-2">
              <Label htmlFor="address.state">UF</Label>
              <Input id="address.state" maxLength={2} {...register("address.state")} />
              <FieldError message={errors.address?.state?.message} />
            </div>
          </div>

          <div className="grid gap-2">
            <Label htmlFor="address.street">Rua</Label>
            <Input id="address.street" {...register("address.street")} />
            <FieldError message={errors.address?.street?.message} />
          </div>

          <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
            <div className="grid gap-2">
              <Label htmlFor="address.neighborhood">Bairro</Label>
              <Input id="address.neighborhood" {...register("address.neighborhood")} />
              <FieldError message={errors.address?.neighborhood?.message} />
            </div>

            <div className="grid gap-2">
              <Label htmlFor="address.number">Numero</Label>
              <Input id="address.number" placeholder="Ex: 123" {...register("address.number")} />
              <FieldError message={errors.address?.number?.message} />
            </div>

            <div className="grid gap-2">
              <Label htmlFor="address.city">Cidade</Label>
              <Input id="address.city" {...register("address.city")} />
              <FieldError message={errors.address?.city?.message} />
            </div>
          </div>

          <Button type="submit" disabled={isPending}>
            {isPending ? "Salvando..." : isEditing ? "Atualizar concessionaria" : "Cadastrar concessionaria"}
          </Button>
        </form>
      </CardContent>
    </Card>
  );
}
