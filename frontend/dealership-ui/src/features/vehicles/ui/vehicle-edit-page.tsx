"use client";

import { useRouter } from "next/navigation";

import { useDealerships } from "@/src/features/dealerships/hooks/use-dealerships";
import { DealershipTableSkeleton } from "@/src/features/dealerships/ui/dealership-table-skeleton";
import { useVehicle } from "@/src/features/vehicles/hooks/use-vehicles";
import { VehicleForm } from "@/src/features/vehicles/ui/vehicle-form";

type VehicleEditPageProps = {
  id: string;
};

export function VehicleEditPage({ id }: VehicleEditPageProps) {
  const router = useRouter();
  const vehicleQuery = useVehicle(id);
  const dealershipsQuery = useDealerships();

  if (vehicleQuery.isLoading || dealershipsQuery.isLoading) {
    return (
      <main className="mx-auto flex w-full max-w-3xl flex-1 flex-col gap-6 px-4 py-8 md:px-8">
        <DealershipTableSkeleton />
      </main>
    );
  }

  return (
    <main className="mx-auto flex w-full max-w-3xl flex-1 flex-col gap-6 px-4 py-8 md:px-8">
      <VehicleForm
        initialData={vehicleQuery.data}
        dealerships={dealershipsQuery.data?.content ?? []}
        onSuccess={() => router.push("/vehicles")}
      />
    </main>
  );
}
