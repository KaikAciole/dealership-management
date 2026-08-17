import { test, expect, type Page, type Route } from '@playwright/test';

test.describe('Fluxo Crítico: Concessionárias', () => {
  test('Deve criar uma concessionária, populando o endereço via ViaCEP', async ({ page }: { page: Page }) => {
    
    // 1. Mock do ViaCEP
    await page.route('**/ws/*/json/', async (route: Route) => {
      await route.fulfill({
        json: { logradouro: 'Avenida Paulista', bairro: 'Bela Vista', localidade: 'São Paulo', uf: 'SP' }
      });
    });

    // 2. Mock da API do Backend para o cadastro de concessionária (evita dependência do OpenCNPJ externo)
    await page.route('**/api/v1/dealerships', async (route: Route) => {
      if (route.request().method() === 'POST') {
        await route.fulfill({
          status: 201,
          json: {
            id: '123e4567-e89b-12d3-a456-426614174000',
            corporateName: 'Auto Premium Motors',
            cnpj: '16670085000155',
            address: { cep: '01310100', street: 'Avenida Paulista', number: '1000', neighborhood: 'Bela Vista', city: 'São Paulo', state: 'SP' },
            isActive: true
          }
        });
      } else {
        await route.continue();
      }
    });

    await page.goto('/dealerships/new');

    await page.getByLabel('Razao social').fill('Auto Premium Motors');
    await page.getByLabel('CNPJ').fill('16.670.085/0001-55');
    
    // Dispara o Blur para chamar o ViaCEP
    await page.getByLabel('CEP').fill('01310-100');
    await page.getByLabel('CEP').blur();

    // Valida se o formulário preencheu os campos automaticamente
    await expect(page.getByLabel('Rua')).toHaveValue('Avenida Paulista');
    await expect(page.getByLabel('Cidade')).toHaveValue('São Paulo');

    await page.getByLabel('Numero').fill('1000');
    
    await page.getByRole('button', { name: /Cadastrar concessionaria/i }).click();

    // Valida o Toast de sucesso e o redirecionamento
    await expect(page.getByText(/Concessionaria cadastrada com sucesso/i)).toBeVisible();
    await expect(page).toHaveURL('/dealerships');
  });
});