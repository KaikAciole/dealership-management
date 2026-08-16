import { VehicleEditPage } from "@/src/features/vehicles/ui/vehicle-edit-page";

type PageProps = {
  params: { id: string };
};

export default function EditVehiclePage({ params }: PageProps) {
  return <VehicleEditPage id={params.id} />;
}
