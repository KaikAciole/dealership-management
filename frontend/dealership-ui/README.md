# Dealership Management UI

Frontend do sistema de gestao de concessionarias e veiculos.

## Stack

- Next.js App Router
- TypeScript strict
- React Query
- React Hook Form + Zod
- Axios
- Tailwind + componentes UI

## Rodando localmente

1. Instale dependencias:

```bash
npm install
```

2. Crie `.env.local`:

```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
NEXT_PUBLIC_MINIO_PUBLIC_URL=http://localhost:9000
```

3. Execute:

```bash
npm run dev
```

4. Validacao de qualidade:

```bash
npm run lint
npm run build
```

## Documentacao interna

- Wiki: [docs/wiki/README.md](docs/wiki/README.md)
- Guia de apresentacao (Q&A): [docs/presentation/qa-guide.md](docs/presentation/qa-guide.md)
