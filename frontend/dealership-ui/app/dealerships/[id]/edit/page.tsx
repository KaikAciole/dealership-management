import { DealershipEditPage } from "@/src/features/dealerships/ui/dealership-edit-page";

type PageProps = {
  params: { id: string };
};

export default function EditDealershipPage({ params }: PageProps) {
  return <DealershipEditPage id={params.id} />;
}
