"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

import { cn } from "@/lib/utils";

const navItems = [
  { href: "/", label: "Home" },
  { href: "/dealerships", label: "Concessionarias" },
  { href: "/vehicles", label: "Veiculos" },
];

export function AppNavbar() {
  const pathname = usePathname();

  return (
    <header className="sticky top-0 z-20 border-b border-sky-100/90 bg-white/95 backdrop-blur-md">
      <div className="mx-auto flex w-full max-w-7xl items-center justify-between px-4 py-3 md:px-8">
        <Link href="/" className="flex items-center gap-2">
          <span className="rounded-md bg-gradient-to-r from-sky-600 to-blue-700 px-2 py-1 text-xs font-semibold text-white">
            DM
          </span>
          <span className="text-sm font-semibold tracking-tight text-slate-800 md:text-base">
            Dealership Management
          </span>
        </Link>

        <nav className="flex items-center gap-1 rounded-xl border border-sky-100 bg-slate-50/90 p-1 shadow-sm">
          {navItems.map((item) => {
            const isActive =
              item.href === "/"
                ? pathname === "/"
                : pathname === item.href || pathname.startsWith(`${item.href}/`);

            return (
              <Link
                key={item.href}
                href={item.href}
                className={cn(
                  "rounded-lg px-3 py-1.5 text-sm transition-all",
                  isActive
                    ? "bg-gradient-to-r from-sky-600 to-blue-700 text-white shadow-sm"
                    : "text-slate-600 hover:bg-white hover:text-slate-900"
                )}
              >
                {item.label}
              </Link>
            );
          })}
        </nav>
      </div>
    </header>
  );
}
