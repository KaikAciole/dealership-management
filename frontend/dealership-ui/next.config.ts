import type { NextConfig } from "next";

const minioPublicUrl =
  process.env.NEXT_PUBLIC_MINIO_PUBLIC_URL ?? "http://localhost:9000";
const parsedMinioUrl = new URL(minioPublicUrl);
const protocol = parsedMinioUrl.protocol.replace(":", "") as "http" | "https";

const nextConfig: NextConfig = {
  images: {
    remotePatterns: [
      {
        protocol,
        hostname: parsedMinioUrl.hostname,
        port: parsedMinioUrl.port,
        pathname: "/**",
      },
    ],
  },
};

export default nextConfig;
