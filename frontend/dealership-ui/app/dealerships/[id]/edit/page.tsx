import { DealershipEditPage } from "@/src/features/dealerships/ui/dealership-edit-page";

type PageProps = {
  params: Promise<{ id: string }>;
};

export default async function EditDealershipPage({ params }: PageProps) {
  const { id } = await params;
  return <DealershipEditPage id={id} />;
}
