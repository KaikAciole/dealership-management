import { VehicleEditPage } from "@/src/features/vehicles/ui/vehicle-edit-page";

type PageProps = {
  params: Promise<{ id: string }>;
};

export default async function EditVehiclePage({ params }: PageProps) {
  const { id } = await params;
  return <VehicleEditPage id={id} />;
}
