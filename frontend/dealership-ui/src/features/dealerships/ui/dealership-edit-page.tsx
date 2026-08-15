"use client";

import { useRouter } from "next/navigation";

import { DealershipForm } from "@/src/features/dealerships/ui/dealership-form";
import { DealershipTableSkeleton } from "@/src/features/dealerships/ui/dealership-table-skeleton";
import { useDealership } from "@/src/features/dealerships/hooks/use-dealerships";

type DealershipEditPageProps = {
  id: string;
};

export function DealershipEditPage({ id }: DealershipEditPageProps) {
  const router = useRouter();
  const dealershipQuery = useDealership(id);

  if (dealershipQuery.isLoading) {
    return (
      <main className="mx-auto flex w-full max-w-3xl flex-1 flex-col gap-6 px-4 py-8 md:px-8">
        <DealershipTableSkeleton />
      </main>
    );
  }

  return (
    <main className="mx-auto flex w-full max-w-3xl flex-1 flex-col gap-6 px-4 py-8 md:px-8">
      <DealershipForm
        initialData={dealershipQuery.data}
        onSuccess={() => router.push("/dealerships")}
      />
    </main>
  );
}
