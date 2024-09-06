import {SecretsManagerClient, GetSecretValueCommand} from "@aws-sdk/client-secrets-manager";

const getSecrets = async (secretName) => {
    const client = new SecretsManagerClient({
        region: "eu-central-1",
    });
    new AWS.Credentials(options)
    try {
        const response = await client.send(
            new GetSecretValueCommand({
                SecretId: secretName,
                VersionStage: "AWSCURRENT",
            })
        );
        return response.SecretString;
    } catch (error) {
        throw error;
    }
};

export default () => {
    const secrets = getSecrets("com/staticSite");
    if(secrets){
        console.log(secrets);
    }

    (g => {
        var h, a, k, p = "The Google Maps JavaScript API", c = "google", l = "importLibrary", q = "__ib__",
            m = document, b = window;
        b = b[c] || (b[c] = {});
        var d = b.maps || (b.maps = {}), r = new Set, e = new URLSearchParams,
            u = () => h || (h = new Promise(async (f, n) => {
                await (a = m.createElement("script"));
                e.set("libraries", [...r] + "");
                for (k in g) e.set(k.replace(/[A-Z]/g, t => "_" + t[0].toLowerCase()), g[k]);
                e.set("callback", c + ".maps." + q);
                a.src = `https://maps.${c}apis.com/maps/api/js?` + e;
                d[q] = f;
                a.onerror = () => h = n(Error(p + " could not load."));
                a.nonce = m.querySelector("script[nonce]")?.nonce || "";
                m.head.append(a);
            }));
        d[l] ? console.warn(p + " only loads once. Ignoring:", g) : d[l] = (f, ...n) => r.add(f) && u().then(() => d[l](f, ...n));
    })({
        key: "AIzaSyA3kcTFb3b2pX6BRjRRn1gvfRW0uBX3c1M",
        v: "weekly",
    });
};