import os
import json

os.environ['GRADIENT_ACCESS_TOKEN'] = "1yK34M2YgfjNfzPRysNTnm9dkNoEBxIh"
os.environ['GRADIENT_WORKSPACE_ID'] = '1a0a874b-1c1e-4b6e-a1da-f137452f8883_workspace'


from cassandra.auth import PlainTextAuthProvider
from cassandra.cluster import Cluster

from llama_index.core import ServiceContext
from llama_index.core import set_global_service_context
from llama_index.core import VectorStoreIndex, SimpleDirectoryReader, StorageContext
from llama_index.embeddings.gradient import GradientEmbedding
from llama_index.llms.gradient import GradientBaseModelLLM
from llama_index.vector_stores.cassandra import CassandraVectorStore



cloud_config= {
  'secure_connect_bundle': 'secure-connect-lavanya-cassendra.zip'
}


with open("Lavanya_Databse_Token.json") as f:
    secrets = json.load(f)

CLIENT_ID = secrets["clientId"]
CLIENT_SECRET = secrets["secret"]

auth_provider = PlainTextAuthProvider(CLIENT_ID, CLIENT_SECRET)
cluster = Cluster(cloud=cloud_config, auth_provider=auth_provider)
session = cluster.connect()

row = session.execute("select release_version from system.local").one()
if row:
  print(row[0])
else:
  print("An error occurred.")
  
llm = GradientBaseModelLLM(
    base_model_slug="llama2-7b-chat",
    max_tokens=400,
)

embed_model = GradientEmbedding(
    gradient_access_token = os.environ["GRADIENT_ACCESS_TOKEN"],
    gradient_workspace_id = os.environ["GRADIENT_WORKSPACE_ID"],
    gradient_model_slug="bge-large",
)
  
service_context = ServiceContext.from_defaults(
    llm = llm,
    embed_model = embed_model,
    chunk_size=256,
)

set_global_service_context(service_context)

documents = SimpleDirectoryReader("Accident_Book").load_data()
print(f"Loaded {len(documents)} document(s).")

index = VectorStoreIndex.from_documents(documents,
                                        service_context=service_context)
def querry_accident(querry):
    query_engine = index.as_query_engine()
    response = query_engine.query(querry)
    return response

